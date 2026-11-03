package uz.davrbank.yeisvoservice.aggregate.mapper.dictionary;

import uz.davrbank.yeisvoservice.aggregate.dto.response.spravochniki.DicContractSubjectsResponse;
import uz.davrbank.yeisvoservice.entity.spravochniki.ContractSubjectsEntity;
import org.springframework.stereotype.Component;

@Component
public class ContractSubjectsMapper {
    public ContractSubjectsEntity toEntity(DicContractSubjectsResponse.Records record) {
        ContractSubjectsEntity entity = new ContractSubjectsEntity();

        entity.setIdNumber(record.getId());
        entity.setName(record.getName());
        entity.setIsDeleted(record.getIsDeleted());
        return entity;
    }

    public DicContractSubjectsResponse.Records toDto(ContractSubjectsEntity save) {
        DicContractSubjectsResponse.Records res = new DicContractSubjectsResponse.Records();
        res.setIdNumber(save.getId());
        res.setId(save.getIdNumber());
        res.setName(save.getName());
        res.setIsDeleted(save.getIsDeleted());
        return res;
    }
}
