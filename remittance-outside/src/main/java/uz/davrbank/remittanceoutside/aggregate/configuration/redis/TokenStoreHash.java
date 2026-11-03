package uz.davrbank.remittanceoutside.aggregate.configuration.redis;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import uz.davrbank.remittanceoutside.aggregate.enums.IntegrationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "tokenStore", timeToLive = 86400)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenStoreHash {

    @Id
    IntegrationType integrationType;

    String value;

    @TimeToLive
    Long expirationInSeconds;
}
