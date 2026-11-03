package uz.davrbank.remittanceoutside.service;

import com.auth0.jwt.JWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import uz.davrbank.remittanceoutside.aggregate.configuration.redis.TokenStoreHash;
import uz.davrbank.remittanceoutside.aggregate.enums.IntegrationType;
import uz.davrbank.remittanceoutside.aggregate.repository.TokenStoreRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenStoreService {

    TokenStoreRepository tokenStoreRepository;

    public String getToken(IntegrationType integrationType) {
        return tokenStoreRepository.findById(integrationType).map(TokenStoreHash::getValue).orElse(null);
    }

    public void setToken(IntegrationType integrationType, String token) {
        TokenStoreHash tokenStoreHash = TokenStoreHash.builder()
                .integrationType(integrationType)
                .value(token)
                .expirationInSeconds(getExpirationInSeconds(token))
                .build();
        tokenStoreRepository.save(tokenStoreHash);
    }

    public static LocalDateTime getExpirationTime(String token) {
        return JWT.decode(token).getExpiresAt()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .minusMinutes(10);
    }


    public static Long getExpirationInSeconds(String token) {
        return getExpirationTime(token).toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
}