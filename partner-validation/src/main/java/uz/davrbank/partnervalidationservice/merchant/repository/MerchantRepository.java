package uz.davrbank.partnervalidationservice.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.davrbank.partnervalidationservice.merchant.entity.Merchant;

import java.util.Optional;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID>, JpaSpecificationExecutor<Merchant> {

    Optional<Merchant> findFirstByMerchantExternalId(String merchantExternalId);

    boolean existsByMerchantExternalId(String merchantExternalId);

    boolean existsByMerchantExternalIdAndIdNot(String merchantExternalId, UUID id);
}
