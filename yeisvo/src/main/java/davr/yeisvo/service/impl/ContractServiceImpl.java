package davr.yeisvo.service.impl;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import davr.yeisvo.dto.TestDtoReq;
import davr.yeisvo.dto.TestDtoRes;
import davr.yeisvo.dto.request.ContractListRequestBody;
import davr.yeisvo.dto.request.ContractRequestBody;
import davr.yeisvo.dto.request.JuridicClientRequest;
import davr.yeisvo.dto.response.*;
import davr.yeisvo.entity.postgres.BankBranchEntity;
import davr.yeisvo.entity.postgres.ContractEntity;
import davr.yeisvo.entity.postgres.ContractGetListEntity;
import davr.yeisvo.external.ExtBusYeisvoService;
import davr.yeisvo.mapper.ContractMapper;
import davr.yeisvo.repository.postgres.ContractRepository;
import davr.yeisvo.mapper.ContractGetListMapper;
import davr.yeisvo.repository.postgres.ContractGetListRepository;
import davr.yeisvo.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Stream;

@Service
@AutoJsonRpcServiceImpl
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {

    private final ContractGetListRepository contractGetListRepository;
    private final ContractMapper contractMapper;
    private final ContractRepository contractRepository;
    private final ContractGetListMapper contractGetListMapper;
    private final ExtBusYeisvoService extBusYeisvoService;
    private final JuridicMFOServiceImpl juridicMFOServiceImpl;
    private final BankBranchServiceImpl bankBranchService;
    private final ContractSaveService contractSaveService;

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
        return extBusYeisvoService.getBankDocs(request);
    }


    @Override
    public ContractGetRes getContract(ContractRequestBody request) {
        ContractGetRes dto = null;
        Optional<ContractEntity> find = contractRepository.findByBankIdAndIdn(request.getBankId(), request.getIdn());
        if (find.isEmpty()) {
            dto = new ContractGetRes();
            dto.setErrorMsg("Contract not found");
            dto.setStatus(0);
        } else {
            dto = contractMapper.toDto(find.get());
            dto.setErrorMsg("OK");
            dto.setStatus(0);
        }
        return dto;
    }

    /**
     * Bu method auto update boladi listga update berganda , va har 12 soatda update bo'ladi
     * va bitta contractni details update qilishda ham shundan foydalanamiz
     *
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
            updatedEntity.setUpdatedDate(LocalDateTime.now());
            saved = contractRepository.save(updatedEntity);
        } else {
            newEntity.setUpdatedDate(LocalDateTime.now());
            saved = contractRepository.save(newEntity);
        }
        log.info("Contract details have finished.\n");
        ContractGetRes dto = contractMapper.toDto(saved);
        dto.setErrorMsg("OK");
        dto.setStatus(0);
        return dto;
    }

    @Override
    public ContractGetOwnerDocRes getContractOwnerDocs(ContractRequestBody request) {
        return extBusYeisvoService.getContractOwnerDocs(request);
    }

    @Override
    public ContractGetHistoryRes getHistoryDocs(ContractRequestBody request) {
        return extBusYeisvoService.getContractHistoryDocs(request);
    }

    /**
     * bu method bitta bank nign contractlarini update qilish kk olganda ishlaydi va update qiladi agar is update true bolsa
     *
     * @param request
     * @return
     */
    @Override
    public ContractGetListWithPageResponse getContractList(ContractListRequestBody request) {
        ContractGetListWithPageResponse res = new ContractGetListWithPageResponse();
        if (request.isUpdate()) {
            if (request.getContractorUzInn() == null || request.getContractorUzInn().isEmpty()) {
                res.setErrorMsg("ContractorUzInn can not be empty when isUpdate = true!");
                res.setStatus(0);
                return res;
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

        Page<ContractGetListEntity> resultPage;

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
        StopWatch totalWatch = new StopWatch("update-contracts");
        totalWatch.start("total");

        log.info("Contract details have started to be updated...\n");

        StopWatch watch = new StopWatch("get-contract-list");
        watch.start();
        ContractGetListRes response = extBusYeisvoService.getContractList(request); //ularning responsi
        watch.stop();
        log.info("getContractList() took {} ms", watch.getTotalTimeMillis());

        final List<ContractGetListEntity> savedEntities = new CopyOnWriteArrayList<>();

        if (response != null && response.getContracts() != null && !response.getContracts().isEmpty()) {

            ExecutorService executor = Executors.newFixedThreadPool(10);
            List<Future<?>> futures = new ArrayList<>();

            for (ContractGetListRes.Contract dto : response.getContracts()) {
                futures.add(executor.submit(() -> {
                    long start = System.currentTimeMillis();

                    ContractGetListEntity incoming = ContractGetListMapper.toEntity(dto, request.getContractorUzInn()); //entity yasadik

                    long findStart = System.currentTimeMillis();
                    Optional<ContractGetListEntity> existingOpt =
                            contractGetListRepository.findByBankIdAndIdn(incoming.getBankId(), incoming.getIdn());
                    long findEnd = System.currentTimeMillis();

                    if (existingOpt.isPresent()) {
                        ContractGetListEntity updateExist = ContractGetListMapper.updateEntity(existingOpt.get(), incoming); //bor bolsa update qilib save qildik
                        savedEntities.add(updateExist);
                    } else {
                        savedEntities.add(incoming);                                            //yoq bolsa save
                    }
                    long updateStart = System.currentTimeMillis();

                    updateContractDetails(
                            new ContractRequestBody(request.getBankInn(), request.getBankId(), incoming.getIdn())
                    );

                    long updateEnd = System.currentTimeMillis();
                    log.info("updateContractDetails() took {} ms for idn={}", (updateEnd - updateStart), incoming.getIdn());
                    log.info("Thread finished: find took {} ms, updateContractDetails took {} ms, total {} ms",
                            (findEnd - findStart), (updateEnd - updateStart), (updateEnd - start));

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

            StopWatch saveWatch = new StopWatch("saveAll");
            saveWatch.start();

            contractGetListRepository.saveAll(savedEntities);

            saveWatch.stop();
            log.info("saveAll() took {} ms", saveWatch.getTotalTimeMillis());
        } else {
            log.info("Contracts are empty...");
        }
        totalWatch.stop();
        log.info("Total update() took {} ms", totalWatch.getTotalTimeMillis());
    }


    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(cron = "0 */10 * * * *")
    public void getContractListUpdate() {
        log.info("Start scheduled contract list update...");

        ExecutorService executor = Executors.newFixedThreadPool(50);
        List<Future<?>> futures = new ArrayList<>();

        try {
            for (BankBranchEntity branch : bankBranchService.allBranches()) {
                try {
                    GetMfoJuridicResponse full = juridicMFOServiceImpl.getJuridicMFO(
                            new JuridicClientRequest(branch.getMfo(), "full"));

                    if (full == null || full.getData() == null) {
                        log.warn("No juridic clients found for branch {}", branch.getMfo());
                        continue;
                    }

                    for (JuridicClient jrd : full.getData()) {
                        ContractListRequestBody request = new ContractListRequestBody();
                        request.setBankId(branch.getMfo());
                        request.setUpdate(true);
                        request.setContractorUzInn(jrd.getCompanyInn());
                        request.setBankInn(branch.getInn());

                        futures.add(executor.submit(() -> processContracts(request, branch, jrd)));
                    }
                } catch (Exception ex) {
                    log.error("Failed to fetch juridic clients for branch={} : {}", branch.getMfo(), ex.getMessage(), ex);
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
     * Bitta bank + client uchun contractlarni qayta ishlash
     */
    private void processContracts(ContractListRequestBody request, BankBranchEntity branch, JuridicClient jrd) {
        try {
            log.info("Fetching contracts for branch={} inn={}", branch.getMfo(), jrd.getCompanyInn());

            ContractGetListRes response = extBusYeisvoService.getContractList(request);
            if (response == null || response.getContracts() == null || response.getContracts().isEmpty()) {
                log.debug("No contracts found for inn={} at branch={}", jrd.getCompanyInn(), branch.getMfo());
                return;
            }

            for (ContractGetListRes.Contract dto : response.getContracts()) {
                ContractGetListEntity entity = ContractGetListMapper.toEntity(dto, request.getContractorUzInn());

                contractSaveService.saveOrUpdate(entity);

                updateContractDetails(new ContractRequestBody(
                        request.getBankInn(),
                        request.getBankId(),
                        entity.getIdn()));
            }
        } catch (Exception e) {
            log.error("Error while processing branch={} inn={} : {}", branch.getMfo(), jrd.getCompanyInn(), e.getMessage(), e);
        }
    }
}
