package davr.yeisvo.service.impl;

import davr.yeisvo.entity.postgres.ContractGetListEntity;
import davr.yeisvo.mapper.ContractGetListMapper;
import davr.yeisvo.repository.postgres.ContractGetListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class ContractSaveService {

    @Autowired
    private ContractGetListRepository contractGetListRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdate(ContractGetListEntity entity) {
        Optional<ContractGetListEntity> existRes =
                contractGetListRepository.findByBankIdAndIdn(entity.getBankId(), entity.getIdn());

        if (existRes.isPresent()) {
            ContractGetListEntity updateExist = ContractGetListMapper.updateEntity(existRes.get(), entity);
            contractGetListRepository.save(updateExist);
            log.info("Updated existing entity with idn: {}", updateExist.getIdn());
        } else {
            contractGetListRepository.save(entity);
            log.info("Created new entity with idn: {}", entity.getIdn());
        }
    }
}

