package uz.davrbank.yeisvoservice.service.service.sevices;

import uz.davrbank.yeisvoservice.aggregate.dto.request.DictionaryBankReq;
import uz.davrbank.yeisvoservice.aggregate.dto.response.spravochniki.*;
import uz.davrbank.yeisvoservice.aggregate.mapper.dictionary.*;
import uz.davrbank.yeisvoservice.entity.spravochniki.*;
import uz.davrbank.yeisvoservice.repository.dictionary.*;
import uz.davrbank.yeisvoservice.service.service.interfaces.DictionaryBankInterface;
import uz.davrbank.yeisvoservice.service.external.ExternalDictionaryBankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DictionaryBankService implements DictionaryBankInterface {
    private final ExternalDictionaryBankService externalDictionaryBankService;
    private final DicCountryRepository dicCountryRepository;
    private final DicCountryMapper dicCountryMapper;
    private final ContractSubjectsMapper contractSubjectsMapper;
    private final DicContractSubjectsRepository dicContractSubjectsRepository;
    private final RegionsRepository regionsRepository;
    private final RegionMapper regionMapper;
    private final IndustrialZoneRepository industrialZoneRepository;
    private final IndustrialZoneMapper industrialZoneMapper;
    private final BankInfoRepository bankInfoRepository;
    private final BankInfoMapper bankInfoMapper;


    /**
     * bu spravochniklar, yani contract details ichidagi contractorForCountryCode
     *
     * @param request
     * @return
     */
    @Override
    public DicCountryResponse dicCountry(DictionaryBankReq request) {
        DicCountryResponse dicCountry = externalDictionaryBankService.getDicCountry(request);
        List<DicCountryResponse.Records> list = new ArrayList<>();

        for (DicCountryResponse.Records records : dicCountry.getRecords()) {

            DicCountryEntity entity = dicCountryRepository.findByIdExt(records.getId())
                    .map(existing -> {
                        existing.setName(records.getName());
                        existing.setIsDeleted(records.getIsDeleted());
                        return existing;
                    })
                    .orElseGet(() -> dicCountryMapper.toEntity(records));

            DicCountryEntity save = dicCountryRepository.save(entity);
            list.add(dicCountryMapper.toDto(save));
        }

        dicCountry.setRecords(list);

        return dicCountry;
    }

    /**
     * bu contract details ichidagi cntrSubject fieldi, u field qiymati nimani anglatishini shu yerdan bilamiz
     *
     * @param request
     * @return
     */
    @Override
    public DicContractSubjectsResponse getDicContractSubjects(DictionaryBankReq request) {
        DicContractSubjectsResponse res = externalDictionaryBankService.getDicContractSubjects(request);
        List<DicContractSubjectsResponse.Records> list = new ArrayList<>();

        for (DicContractSubjectsResponse.Records records : res.getRecords()) {
            ContractSubjectsEntity entity = dicContractSubjectsRepository.getByIdExt(records.getId())
                    .map(existing -> {
                        existing.setName(records.getName());
                        existing.setIsDeleted(records.getIsDeleted());
                        return existing;
                    })
                    .orElseGet(() -> contractSubjectsMapper.toEntity(records));

            list.add(contractSubjectsMapper.toDto(dicContractSubjectsRepository.save(entity)));
        }

        res.setRecords(list);
        return res;
    }


    @Override
    public DicCountryResponse getDicCountryById(String id) {
        DicCountryResponse response = new DicCountryResponse();

        Optional<DicCountryEntity> entityOpt = dicCountryRepository.findByIdExt(id);
        if (entityOpt.isPresent()) {
            DicCountryEntity entity = entityOpt.get();
            response.setErrorMsg("OK");
            response.setStatus(0);

            DicCountryResponse.Records dto = dicCountryMapper.toDto(entity);
            response.setRecords(Collections.singletonList(dto));
        } else {
            response.setErrorMsg("Not Found");
            response.setStatus(1);
            response.setRecords(Collections.emptyList());
        }

        return response;
    }

    @Override
    public DicContractSubjectsResponse getDicContractSubjectsById(Long id) {
        DicContractSubjectsResponse response = new DicContractSubjectsResponse();

        Optional<ContractSubjectsEntity> entityOpt = dicContractSubjectsRepository.getByIdExt(id);
        if (entityOpt.isPresent()) {
            ContractSubjectsEntity entity = entityOpt.get();
            DicContractSubjectsResponse.Records dto = contractSubjectsMapper.toDto(entity);
            response.setRecords(Collections.singletonList(dto));
            response.setErrorMsg("OK");
            response.setStatus(0);
        } else {
            response.setRecords(Collections.emptyList());
            response.setErrorMsg("Not Found");
            response.setStatus(1);
        }
        return response;
    }

    @Override
    public RegionsResponse dicRegion(DictionaryBankReq request) {
        RegionsResponse regionsResponse =  externalDictionaryBankService.getRegion(request);;
        List<RegionsResponse.Regions> list = new ArrayList<>();

        for (RegionsResponse.Regions regions : regionsResponse.getRecords()) {
            if (regions.getId() != null) {
                Optional<RegionsEntity> existingOpt = regionsRepository.findByIdExt(regions.getId());

                RegionsEntity entity = null;
                if (existingOpt.isPresent()) {
                    entity = regionMapper.update(regions, existingOpt.get());
                } else {
                    entity = regionMapper.toEntity(regions);
                }

                regionsRepository.save(entity);
                list.add(RegionMapper.toDto(entity));
            }
        }

        regionsResponse.setRecords(list);
        regionsResponse.setStatus(0);
        regionsResponse.setErrorMsg("OK");

        return regionsResponse;
    }

    @Override
    public IndustrialZoneResponse dicIndustrialZone(DictionaryBankReq request) {
        IndustrialZoneResponse industrialZoneResponse = new IndustrialZoneResponse();
        List<IndustrialZoneResponse.IndustrialZone> list = new ArrayList<>();

        IndustrialZoneResponse externalResponse = externalDictionaryBankService.getIndustrialZone(request);
        for (IndustrialZoneResponse.IndustrialZone industrialZone : externalResponse.getRecords()) {
            if (industrialZone.getId() != null) {
                Optional<IndustrialZoneEntity> existingOpt = industrialZoneRepository.findByIdExt(industrialZone.getId());

                IndustrialZoneEntity entity = null;
                if (existingOpt.isPresent()) {
                    entity = industrialZoneMapper.update(industrialZone, existingOpt.get());
                } else {
                    entity = industrialZoneMapper.toEntity(industrialZone);
                }

                industrialZoneRepository.save(entity);
                list.add(industrialZoneMapper.toDto(entity));
            }
        }

        industrialZoneResponse.setRecords(list);
        industrialZoneResponse.setStatus(0);
        industrialZoneResponse.setErrorMsg("OK");

        return industrialZoneResponse;
    }

    @Override
    public DictionaryBankResponse dicBankResponse(DictionaryBankReq request) {
        DictionaryBankResponse dictionaryBankResponse = new DictionaryBankResponse();
        List<DictionaryBankResponse.DictionaryBank> list = new ArrayList<>();

        DictionaryBankResponse externalResponse = externalDictionaryBankService.getBankInfo(request);
        for (DictionaryBankResponse.DictionaryBank dictionaryBank : externalResponse.getBanks()) {
            if (dictionaryBank.getBankInn() != null && dictionaryBank.getBankId() != null) {
                Optional<BankInfoEntity> existingOpt = bankInfoRepository.findByBankInnAndBankId(dictionaryBank.getBankInn(), dictionaryBank.getBankId());

                BankInfoEntity entity = null;
                if (existingOpt.isPresent()) {
                    entity = bankInfoMapper.update(dictionaryBank, existingOpt.get());
                } else {
                    entity = bankInfoMapper.toEntity(dictionaryBank);
                }

                bankInfoRepository.save(entity);
                list.add(BankInfoMapper.toDto(entity));
            }
        }

        dictionaryBankResponse.setBanks(list);
        dictionaryBankResponse.setStatus(0);
        dictionaryBankResponse.setErrorMsg("OK");

        return dictionaryBankResponse;
    }

    @Override
    public RegionsResponse getDicRegionById(String id) {
        RegionsResponse response = new RegionsResponse();

        Optional<RegionsEntity> entityOpt = regionsRepository.findByIdExt(id);
        if (entityOpt.isPresent()) {
            RegionsEntity entity = entityOpt.get();
            response.setErrorMsg("OK");
            response.setStatus(0);

            RegionsResponse.Regions dto = RegionMapper.toDto(entity);
            response.setRecords(Collections.singletonList(dto));
        } else {
            response.setErrorMsg("Not Found");
            response.setStatus(1);
            response.setRecords(Collections.emptyList());
        }

        return response;
    }

    @Override
    public IndustrialZoneResponse getDicIndustrialZoneById(Long id) {
        IndustrialZoneResponse response = new IndustrialZoneResponse();

        Optional<IndustrialZoneEntity> entityOpt = industrialZoneRepository.findByIdExt(id);
        if (entityOpt.isPresent()) {
            IndustrialZoneEntity entity = entityOpt.get();
            response.setErrorMsg("OK");
            response.setStatus(0);

            IndustrialZoneResponse.IndustrialZone dto = industrialZoneMapper.toDto(entity);
            response.setRecords(Collections.singletonList(dto));
        } else {
            response.setErrorMsg("Not Found");
            response.setStatus(1);
            response.setRecords(Collections.emptyList());
        }

        return response;
    }

    @Override
    public DictionaryBankResponse getDicBankId(Long id) {
        DictionaryBankResponse response = new DictionaryBankResponse();

        Optional<BankInfoEntity> entityOpt = bankInfoRepository.getByIdExt(id);
        if (entityOpt.isPresent()) {
            BankInfoEntity entity = entityOpt.get();
            response.setErrorMsg("OK");
            response.setStatus(0);

            DictionaryBankResponse.DictionaryBank dto = BankInfoMapper.toDto(entity);
            response.setBanks(Collections.singletonList(dto));
        } else {
            response.setErrorMsg("Not Found");
            response.setStatus(1);
            response.setBanks(Collections.emptyList());
        }

        return response;
    }
}




