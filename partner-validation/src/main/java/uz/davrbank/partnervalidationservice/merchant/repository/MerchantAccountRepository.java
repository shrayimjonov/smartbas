package uz.davrbank.partnervalidationservice.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.davrbank.partnervalidationservice.merchant.entity.MerchantAccount;
import uz.davrbank.partnervalidationservice.merchant.enums.Currency;

import java.util.Optional;
import java.util.UUID;

public interface MerchantAccountRepository extends JpaRepository<MerchantAccount, UUID>, JpaSpecificationExecutor<MerchantAccount> {

    Optional<MerchantAccount> findByMerchantIdAndId(UUID merchantId, UUID id);

    Optional<MerchantAccount> findFirstByMerchantIdAndAccountNumber(UUID merchantId, String accountNumber);

    Optional<MerchantAccount> findFirstByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByMerchantIdAndAccountNumberAndCurrencyAndIdNot(UUID merchantId, String accountNumber, Currency currency, UUID id);

    boolean existsByMerchantIdAndAccountNumberAndCurrency(UUID merchantId, String accountNumber, Currency currency);
}
