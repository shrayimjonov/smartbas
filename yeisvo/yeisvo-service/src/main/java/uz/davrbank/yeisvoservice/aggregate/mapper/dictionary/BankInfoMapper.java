package uz.davrbank.yeisvoservice.aggregate.mapper.dictionary;

import uz.davrbank.yeisvoservice.aggregate.dto.response.spravochniki.DictionaryBankResponse;
import uz.davrbank.yeisvoservice.entity.spravochniki.BankInfoEntity;
import org.springframework.stereotype.Component;

@Component
public class BankInfoMapper {
    public BankInfoEntity toEntity(DictionaryBankResponse.DictionaryBank dictionaryBank) {
        BankInfoEntity entity = new BankInfoEntity();
        entity.setBankInn(dictionaryBank.getBankInn());
        entity.setBankId(dictionaryBank.getBankId());
        entity.setBankClass(dictionaryBank.getBankClass());
        entity.setRegionId(dictionaryBank.getRegionId());
        entity.setBankType(dictionaryBank.getBankType());
        entity.setBankName(dictionaryBank.getBankName());
        entity.setBankAddress(dictionaryBank.getBankAddress());
        entity.setActive(dictionaryBank.getActive());
        entity.setNewBankInn(dictionaryBank.getNewBankInn());
        entity.setNewBankId(dictionaryBank.getNewBankId());
        entity.setParentNewBankInn(dictionaryBank.getParentNewBankInn());
        entity.setParentNewBankId(dictionaryBank.getParentNewBankId());

        return entity;
    }

    public BankInfoEntity update (DictionaryBankResponse.DictionaryBank externalResponse, BankInfoEntity existingOpt) {

        existingOpt.setBankClass(externalResponse.getBankClass());
        existingOpt.setRegionId(externalResponse.getRegionId());
        existingOpt.setBankType(externalResponse.getBankType());
        existingOpt.setBankName(externalResponse.getBankName());
        existingOpt.setBankAddress(externalResponse.getBankAddress());
        existingOpt.setActive(externalResponse.getActive());
        existingOpt.setNewBankInn(externalResponse.getNewBankInn());
        existingOpt.setNewBankId(externalResponse.getNewBankId());
        existingOpt.setParentNewBankInn(externalResponse.getParentNewBankInn());
        existingOpt.setParentNewBankId(externalResponse.getParentNewBankId());
        existingOpt.setBankId(externalResponse.getBankId());
        existingOpt.setBankInn(externalResponse.getBankInn());
        return existingOpt;
    }

    public static DictionaryBankResponse.DictionaryBank toDto(BankInfoEntity entity) {
        DictionaryBankResponse.DictionaryBank dto = new DictionaryBankResponse.DictionaryBank();
        dto.setId(entity.getId()); // bu bizdan qaytariladigan id
        dto.setBankInn(entity.getBankInn());
        dto.setBankId(entity.getBankId());
        dto.setBankClass(entity.getBankClass());
        dto.setRegionId(entity.getRegionId());
        dto.setBankType(entity.getBankType());
        dto.setBankName(entity.getBankName());
        dto.setBankAddress(entity.getBankAddress());
        dto.setActive(entity.getActive());
        dto.setNewBankInn(entity.getNewBankInn());
        dto.setNewBankId(entity.getNewBankId());
        dto.setParentNewBankInn(entity.getParentNewBankInn());
        dto.setParentNewBankId(entity.getParentNewBankId());
        return dto;
    }
}

