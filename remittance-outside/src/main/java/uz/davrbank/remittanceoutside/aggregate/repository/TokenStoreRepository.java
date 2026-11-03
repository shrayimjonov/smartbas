package uz.davrbank.remittanceoutside.aggregate.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.remittanceoutside.aggregate.configuration.redis.TokenStoreHash;
import uz.davrbank.remittanceoutside.aggregate.enums.IntegrationType;

@Repository
public interface TokenStoreRepository extends CrudRepository<TokenStoreHash, IntegrationType> {
}
