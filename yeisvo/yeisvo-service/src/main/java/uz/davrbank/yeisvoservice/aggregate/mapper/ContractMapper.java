package uz.davrbank.yeisvoservice.aggregate.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uz.davrbank.yeisvoservice.aggregate.dto.response.ContractGetRes;
import uz.davrbank.yeisvoservice.entity.ContractEntity;
import uz.davrbank.yeisvoservice.exception.controller.GlobalExceptionMessage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class ContractMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ModelMapper modelMapper = new ModelMapper();

    public ContractEntity toEntity(ContractGetRes res) {
        if (res == null) {
            return null;
        }

        ContractEntity entity = modelMapper.map(res.getContract(), ContractEntity.class);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tashkent"));
        entity.setUpdatedDate(now);
        entity.setCreatedDate(now);

        try {
            String json = objectMapper.writeValueAsString(res);
            entity.setData(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON ni saqlashda xatolik", e);
        }
        return entity;
    }

    public ContractGetRes toDto(ContractEntity entity) {
        if (entity == null) {
            return null;
        }
        try {
            ContractGetRes contractGetRes = objectMapper.readValue(entity.getData(), ContractGetRes.class);
            contractGetRes.getContract().setId(entity.getId());
            contractGetRes.getContract().setUpdatedDate(entity.getUpdatedDate());
            contractGetRes.getContract().setCreatedDate(entity.getCreatedDate());
            return contractGetRes;
        } catch (Exception e) {
            throw new GlobalExceptionMessage(400, "Error reading JSON data", e);
        }
    }

    public ContractEntity update(ContractEntity existing, ContractEntity incoming) {
        existing.setRootIdn(incoming.getRootIdn());
        existing.setCntrType(incoming.getCntrType());
        existing.setOldIdn(incoming.getOldIdn());
        existing.setBankId(incoming.getBankId());
        existing.setBankInn(incoming.getBankInn());
        existing.setMvesNo(incoming.getMvesNo());
        existing.setMvesDate(incoming.getMvesDate());
        existing.setGovDcsnNo(incoming.getGovDcsnNo());
        existing.setGovDcsnDate(incoming.getGovDcsnDate());
        existing.setOkpo(incoming.getOkpo());
        existing.setOkonx(incoming.getOkonx());
        existing.setContractorUzParentCompany(incoming.getContractorUzParentCompany());
        existing.setContractorUzName(incoming.getContractorUzName());
        existing.setRgnCode(incoming.getRgnCode());
        existing.setIndustrialZoneCode(incoming.getIndustrialZoneCode());
        existing.setContractorUzAddress(incoming.getContractorUzAddress());
        existing.setContractorUzPhone(incoming.getContractorUzPhone());
        existing.setCntrSubject(incoming.getCntrSubject());
        existing.setCurrCode1(incoming.getCurrCode1());
        existing.setCurrCode2(incoming.getCurrCode2());
        existing.setAmount1(incoming.getAmount1());
        existing.setAmount2(incoming.getAmount2());
        existing.setAccCurrCode1(incoming.getAccCurrCode1());
        existing.setAccCurrCode2(incoming.getAccCurrCode2());
        existing.setContractorForName(incoming.getContractorForName());
        existing.setContractorForCountryCode(incoming.getContractorForCountryCode());
        existing.setContractorForAddress(incoming.getContractorForAddress());
        existing.setBankForName(incoming.getBankForName());
        existing.setBankForCountryCode(incoming.getBankForCountryCode());
        existing.setBankForAddress(incoming.getBankForAddress());
        existing.setBankForAttributes(incoming.getBankForAttributes());
        existing.setCentralBankLetterNo(incoming.getCentralBankLetterNo());
        existing.setCentralBankLetterDate(incoming.getCentralBankLetterDate());
        existing.setCreditRate(incoming.getCreditRate());
        existing.setLeasingFlag(incoming.getLeasingFlag());
        existing.setProductsName(incoming.getProductsName());
        existing.setDocNo(incoming.getDocNo());
        existing.setDocDate(incoming.getDocDate());
        existing.setExecType(incoming.getExecType());
        existing.setExecDate(incoming.getExecDate());
        existing.setReestrRegNo(incoming.getReestrRegNo());
        existing.setSoftwareName(incoming.getSoftwareName());
        existing.setCntrStatus(incoming.getCntrStatus());
        existing.setLockDate(incoming.getLockDate());
        existing.setStatus(incoming.getStatus());
        existing.setCstDate(incoming.getCstDate());
        existing.setContractorUzInn(incoming.getContractorUzInn());
        existing.setReceivableTotal1(incoming.getReceivableTotal1());
        existing.setReceivableOverdue1(incoming.getReceivableOverdue1());
        existing.setReceivableTotal2(incoming.getReceivableTotal2());
        existing.setReceivableOverdue2(incoming.getReceivableOverdue2());
        existing.setPayableTotal1(incoming.getPayableTotal1());
        existing.setPayableOverdue1(incoming.getPayableOverdue1());
        existing.setPayableTotal2(incoming.getPayableTotal2());
        existing.setPayableOverdue2(incoming.getPayableOverdue2());
        existing.setInsTime(incoming.getInsTime());
        existing.setData(incoming.getData());

        return existing;
    }
}
