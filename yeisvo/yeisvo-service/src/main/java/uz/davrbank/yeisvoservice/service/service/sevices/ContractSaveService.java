package uz.davrbank.yeisvoservice.service.service.sevices;

import uz.davrbank.yeisvoservice.entity.ContractListEntity;
import uz.davrbank.yeisvoservice.aggregate.mapper.ContractGetListMapper;
import uz.davrbank.yeisvoservice.repository.ContractGetListRepository;
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
    public void saveOrUpdate(ContractListEntity entity) {
        Optional<ContractListEntity> existRes =
                contractGetListRepository.findByBankIdAndIdn(entity.getBankId(), entity.getIdn());

        if (existRes.isPresent()) {
            ContractListEntity updateExist = ContractGetListMapper.updateEntity(existRes.get(), entity);
            contractGetListRepository.save(updateExist);
            log.info("Updated existing entity with idn: {}", updateExist.getIdn());
        } else {
            contractGetListRepository.save(entity);
            log.info("Created new entity with idn: {}", entity.getIdn());
        }
    }
}

