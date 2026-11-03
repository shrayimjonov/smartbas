package uz.davrbank.payment.service.core.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.payment.model.entity.Bill;
import uz.davrbank.payment.repository.BillRepository;
import uz.davrbank.payment.service.core.BillService;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillServiceImpl implements BillService {
    private final BillRepository repository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Bill save(Bill bill) {
        return repository.save(bill);
    }

    @Override
    @Transactional(readOnly = true)
    public Bill findByTranId(Long tranId) {
        log.info("Find bill by tranId: {}", tranId);
        return repository.findByTranId(tranId)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found for transaction: %d".formatted(tranId)));

    }
}
