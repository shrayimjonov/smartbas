package uz.davrbank.payment.service.core.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.payment.model.entity.Transaction;
import uz.davrbank.payment.repository.TransactionRepository;
import uz.davrbank.payment.service.core.TransactionService;
import uz.nodir.commondto.exception.EntityNotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;

    @Transactional
    @Override
    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

    @Transactional(readOnly = true)
    @Override
    public Transaction findById(Long id) {
        log.info("Find transaction by id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment with id: %d not found!".formatted(id)));
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<Transaction> findTransactionOptionalByInternalId(String internalId) {
        log.info("Find transaction by internal id: {}", internalId);
        return repository.findByInternalId(internalId);
    }


}
