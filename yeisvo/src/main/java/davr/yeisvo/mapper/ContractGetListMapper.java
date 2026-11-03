package davr.yeisvo.mapper;

import davr.yeisvo.dto.response.ContractGetListRes;
import davr.yeisvo.entity.postgres.ContractGetListEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Component
public class ContractGetListMapper {
    public static ContractGetListEntity toEntity(ContractGetListRes.Contract dto, String contractorUzInn) {
        ContractGetListEntity entity = new ContractGetListEntity();
        entity.setIdn(dto.getIdn());
        entity.setBankInn(dto.getBankInn());
        entity.setBankId(dto.getBankId());
        entity.setCntrStatus(dto.getCntrStatus());
        entity.setContractorUzName(dto.getContractorUzName());
        entity.setDocNo(dto.getDocNo());
        entity.setDocDate(String.valueOf(LocalDate.parse(dto.getDocDate()))); // format yyyy-MM-dd
        entity.setContractorForName(dto.getContractorForName());
        entity.setContractorForCountryCode(dto.getContractorForCountryCode());
        entity.setContractorUzInn(contractorUzInn);
        return entity;
    }

    public static ContractGetListEntity updateEntity(ContractGetListEntity existing, ContractGetListEntity incoming) {
        existing.setIdn(incoming.getIdn());
        existing.setBankInn(incoming.getBankInn());
        existing.setBankId(incoming.getBankId());
        existing.setCntrStatus(incoming.getCntrStatus());
        existing.setContractorUzName(incoming.getContractorUzName());
        existing.setDocNo(incoming.getDocNo());
        existing.setDocDate(incoming.getDocDate());
        existing.setContractorForName(incoming.getContractorForName());
        existing.setContractorForCountryCode(incoming.getContractorForCountryCode());
        existing.setContractorUzInn(incoming.getContractorUzInn());
        return existing;
    }

    public  List<ContractGetListRes.Contract> toDtoList(List<ContractGetListEntity> list, boolean isUpdate) {
        List<ContractGetListRes.Contract> dtoList = new ArrayList<>();
        if (list != null) {
            for (ContractGetListEntity entity : list) {
                ContractGetListRes.Contract dto = new ContractGetListRes.Contract();

                dto.setIdn(entity.getIdn());
                dto.setBankInn(entity.getBankInn());
                dto.setBankId(entity.getBankId());
                dto.setCntrStatus(entity.getCntrStatus());
                dto.setContractorUzName(entity.getContractorUzName());
                dto.setDocNo(entity.getDocNo());
                dto.setDocDate(String.valueOf(LocalDate.parse(entity.getDocDate()))); // format yyyy-MM-dd
                dto.setContractorForName(entity.getContractorForName());
                dto.setContractorForCountryCode(entity.getContractorForCountryCode());
                dto.setUpdate(isUpdate);
                dto.setContractorUzInn(entity.getContractorUzInn());
                dtoList.add(dto);
            }
        }
        return dtoList;
    }




}
