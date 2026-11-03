package uz.davrbank.yeisvoservice.service.service.sevices;

import org.springframework.http.HttpStatus;
import uz.davrbank.yeisvoservice.aggregate.dto.request.TestDtoReq;
import uz.davrbank.yeisvoservice.aggregate.dto.response.*;
import uz.davrbank.yeisvoservice.aggregate.dto.request.ContractListRequestBody;
import uz.davrbank.yeisvoservice.aggregate.dto.request.ContractRequestBody;
import uz.davrbank.yeisvoservice.aggregate.dto.request.JuridicClientRequest;
import uz.davrbank.yeisvoservice.aggregate.dto.response.report.ResponseDTO;
import uz.davrbank.yeisvoservice.entity.BankInfo;
import uz.davrbank.yeisvoservice.entity.ContractEntity;
import uz.davrbank.yeisvoservice.entity.ContractListEntity;
import uz.davrbank.yeisvoservice.exception.controller.GlobalExceptionMessage;
import uz.davrbank.yeisvoservice.service.service.interfaces.ContractInterface;
import uz.davrbank.yeisvoservice.service.external.ExtBusYeisvoService;
import uz.davrbank.yeisvoservice.aggregate.mapper.ContractMapper;
import uz.davrbank.yeisvoservice.repository.ContractRepository;
import uz.davrbank.yeisvoservice.aggregate.mapper.ContractGetListMapper;
import uz.davrbank.yeisvoservice.repository.ContractGetListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractService implements ContractInterface {

    private final ContractGetListRepository contractGetListRepository;
    private final ContractMapper contractMapper;
    private final ContractRepository contractRepository;
    private final ContractGetListMapper contractGetListMapper;
    private final ExtBusYeisvoService extBusYeisvoService;
    private final JuridicMFOService juridicMFOService;
    private final ContractSaveService contractSaveService;

    private static final Set<BankInfo> ALLOWED_BANKS = Set.of(
            new BankInfo("00981", "203709707"),
            new BankInfo("01122", "207212342"),
            new BankInfo("01046", "206973924"),
            new BankInfo("01069", "207076924"),
            new BankInfo("01072", "207094902"),
            new BankInfo("01086", "207128010"),
            new BankInfo("01121", "207212359"));


    @Override
    public TestDtoRes checkContract(TestDtoReq request) {
        TestDtoRes res = new TestDtoRes();
        res.setUrl("this url");
        res.setSomething("test");
        return res;
    }

    @Override
    public ContractGetStatusRes getContractStatus(ContractRequestBody request) {
        return extBusYeisvoService.getContractStatus(request);
    }


    @Override
    public ContractGetBankDocsRes getBankDocs(ContractRequestBody request) {
        log.info("Request came to getBankDocs {}", request);
        return extBusYeisvoService.getBankDocs(request);
    }


    @Override
    public ContractGetRes getContract(ContractRequestBody request) {
        ContractGetRes dto;
        Optional<ContractEntity> find = contractRepository.findByBankIdAndIdn(request.getBankId(), request.getIdn());
        if (find.isEmpty()) {
            dto = new ContractGetRes();
            dto.setErrorMsg("Contract not found");
            log.error("Contract not found with data{}", request.getIdn() );
            dto.setStatus(0);
        } else {
            dto = contractMapper.toDto(find.get());
            dto.setErrorMsg("OK");
            dto.setStatus(0);
            log.info("Contract found with data{}", request.getIdn() );
        }
        return dto;
    }

    /**
     * Bu method auto update boladi listga update berganda , va har 12 soatda update bo'ladi
     * va bitta contractni details update qilishda ham shundan foydalanamiz
     * @param request
     * @return
     */
    public ContractGetRes updateContractDetails(ContractRequestBody request) {
        log.info("Contract details have started to be updated...\n");
        ContractGetRes externalRes = extBusYeisvoService.getContract(request);
        if (externalRes == null) {
            ContractGetRes res = new ContractGetRes();
            res.setErrorMsg("Contract not found");
            res.setStatus(0);
            return res;
        }

        ContractEntity newEntity = contractMapper.toEntity(externalRes);

        ContractEntity saved;
        Optional<ContractEntity> existing = contractRepository.findByIdn(newEntity.getIdn());

        if (existing.isPresent()) {
            ContractEntity updatedEntity = contractMapper.update(existing.get(), newEntity);

            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tashkent"));
            updatedEntity.setUpdatedDate(now);

            saved = contractRepository.save(updatedEntity);
            log.info("Contract details have updated...\n");
        } else {
            newEntity.setUpdatedDate(LocalDateTime.now());
            saved = contractRepository.save(newEntity);
            log.info("Contract details saved...\n");
        }
        log.info("Contract details have finished.\n");
        ContractGetRes dto = contractMapper.toDto(saved);
        dto.setErrorMsg("OK");
        dto.setStatus(0);
        return dto;
    }

    @Override
    public ContractGetOwnerDocRes getContractOwnerDocs(ContractRequestBody request) {
        log.info("Request came to getContractOwnerDocs {}", request);
        return extBusYeisvoService.getContractOwnerDocs(request);
    }

    @Override
    public ContractGetHistoryRes getHistoryDocs(ContractRequestBody request) {
        log.info("Request came to getHistoryDocs {}", request);
        return extBusYeisvoService.getContractHistoryDocs(request);
    }

    /**
     * bu method bitta bank ning contractlarini update qilish kk olganda ishlaydi va update qiladi agar is update true bolsa
     *
     * @param request
     * @return
     */
    @Override
    public ContractGetListWithPageResponse getContractList(ContractListRequestBody request) {
        ContractGetListWithPageResponse res = new ContractGetListWithPageResponse();
        if (request.isUpdate()) {
            if (request.getContractorUzInn() == null || request.getContractorUzInn().isEmpty()) {
                throw new GlobalExceptionMessage(400, "ContractorUzInn can not be empty when isActive = true", HttpStatus.BAD_REQUEST);
            }
             update(request);
        }

        boolean isSearch = Stream.of(
                request.getBankId(),
                request.getContractorUzInn(),
                request.getContractorUzName(),
                request.getDocNo(),
                request.getIdn()
        ).anyMatch(value -> value != null && !value.isEmpty());

        Page<ContractListEntity> resultPage;

        int page = Math.max(request.getPage(), 0);
        int size = request.getSize() > 0 ? request.getSize() : 20;
        Pageable pageable = PageRequest.of(page, size);

        if (isSearch) {
            resultPage = contractGetListRepository.searchContracts(
                    request.getBankInn(),
                    request.getBankId(),
                    request.getContractorUzInn(),
                    request.getDocNo(),
                    request.getIdn(),
                    request.getContractorUzName(),
                    pageable
            );
        } else {
            resultPage = contractGetListRepository.findAll(pageable);
        }
        res.setContracts(contractGetListMapper.toDtoList(resultPage.getContent(), request.isUpdate()));
        res.setPage(request.getPage());
        res.setSize(request.getSize());
        res.setTotal(resultPage.getTotalElements());
        res.setErrorMsg("OK");
        res.setStatus(0);
        return res;
    }


    /**
     * bu method update qilishni hohlaganda update qiladi va yangilangan malumot qaytaradi
     *
     * @param request
     * @return
     */
    private void update(ContractListRequestBody request) {
        log.info("Contract details have started to be updated...\n");

        ContractGetListRes response = extBusYeisvoService.getContractList(request); //ularning responsi
        final List<ContractListEntity> savedEntities = new CopyOnWriteArrayList<>();

        if (response != null && response.getContracts() != null && !response.getContracts().isEmpty()) {

            ExecutorService executor = Executors.newFixedThreadPool(10);
            List<Future<?>> futures = new ArrayList<>();

            for (ContractGetListRes.Contract dto : response.getContracts()) {
                futures.add(executor.submit(() -> {
                    ContractListEntity incoming = ContractGetListMapper.toEntity(dto, request.getContractorUzInn()); //entity yasadik

                    Optional<ContractListEntity> existingOpt =
                            contractGetListRepository.findByBankIdAndIdn(incoming.getBankId(), incoming.getIdn());
                    if (existingOpt.isPresent()) {
                        ContractListEntity updateExist = ContractGetListMapper.updateEntity(existingOpt.get(), incoming); //bor bolsa update qilib save qildik
                        savedEntities.add(updateExist);
                        log.info("Contract details is found and added to list...\n");
                    } else {
                        log.info("Income Contract details added to list \n");
                        savedEntities.add(incoming);                                            //yoq bolsa save
                    }

                    updateContractDetails(
                            new ContractRequestBody(request.getBankInn(), request.getBankId(), incoming.getIdn())
                    );

                }));
            }

            for (Future<?> f : futures) {
                try {
                    f.get();
                } catch (Exception e) {
                    log.error("Thread execution failed: {}", e.getMessage());
                }
            }
            executor.shutdown();

            contractGetListRepository.saveAll(savedEntities);
            log.info("Saved all contract list.");
        } else {
            log.info("Contracts are empty...");
        }
    }


    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(cron = "0 */10 * * * *")
    public void getContractListUpdate() {
        log.info("Start scheduled contract list update...");

        ExecutorService executor = Executors.newFixedThreadPool(50);
        List<Future<?>> futures = new ArrayList<>();
        try {
            for (BankInfo branch : ALLOWED_BANKS) {
                try {
                    log.info("Start getting JuridicClients ...\n");
                    ResponseDTO<List<ResponseInnDTO>> activeList = juridicMFOService.getJuridicMFO(
                            new JuridicClientRequest(branch.bankId(), 2));
                    ResponseDTO<List<ResponseInnDTO>> closedList = juridicMFOService.getJuridicMFO(
                            new JuridicClientRequest(branch.bankId(), 3));

                    List<ResponseInnDTO> fullList = new ArrayList<>();

                    if (activeList != null && activeList.getData() != null) {
                        fullList.addAll(activeList.getData());
                    }
                    if (closedList != null && closedList.getData() != null) {
                        fullList.addAll(closedList.getData());
                    }

                    if (fullList.isEmpty()) {
                        log.warn("No juridic clients found for branch {}", branch.bankId());
                        continue;
                    }
                    log.info("Juridic clients found for branch {}", branch.bankId());

                    for (ResponseInnDTO jrd : fullList) {
                        ContractListRequestBody request = new ContractListRequestBody();
                        request.setBankId(branch.bankId());
                        request.setUpdate(true);
                        request.setContractorUzInn(jrd.getInn());
                        request.setBankInn(branch.bankInn());

                        futures.add(executor.submit(() -> processContracts(request, branch.bankId(), jrd)));
                    }
                } catch (Exception ex) {
                    log.error("Failed to fetch juridic clients for branch={} : {}", branch.bankId(), ex.getMessage(), ex);
                }
            }

            // Hamma thread tugashini kutamiz
            for (Future<?> f : futures) {
                try {
                    f.get(); // blokirovka qilib kutadi
                } catch (Exception e) {
                    log.error("Thread execution failed: {}", e.getMessage(), e);
                }
            }
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            log.info("Finished scheduled contract list update.");
        }
    }


    /**
     *
     * Bitta bank + client uchun contractlarni qayta ishlash
     */
    private void processContracts(ContractListRequestBody request, String branchId, ResponseInnDTO jrd) {
        try {
            log.info("Fetching contracts for branch={} inn={}", branchId, jrd.getInn());

            ContractGetListRes response = extBusYeisvoService.getContractList(request);
            if (response == null || response.getContracts() == null || response.getContracts().isEmpty()) {
                log.debug("No contracts found for inn={} at branch={}", jrd.getInn(), branchId);
                return;
            }

            for (ContractGetListRes.Contract dto : response.getContracts()) {
                ContractListEntity entity = ContractGetListMapper.toEntity(dto, request.getContractorUzInn());

                contractSaveService.saveOrUpdate(entity);

                updateContractDetails(new ContractRequestBody(
                        request.getBankInn(),
                        request.getBankId(),
                        entity.getIdn()));
            }
        } catch (Exception e) {
            log.error("Error while processing branch={} inn={} : {}", branchId, jrd.getInn(), e.getMessage(), e);
        }
    }
}
