package uz.davrbank.payment.service.core;

import uz.davrbank.payment.model.entity.Transaction;

import java.util.Optional;

public interface TransactionService {
    Transaction save(Transaction transaction);

    Transaction findById(Long id);

    Optional<Transaction> findTransactionOptionalByInternalId(String internalId);
}
