package uz.davrbank.partnervalidationservice.gateway;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import uz.davrbank.partnervalidationservice.client.GatewayAccessToken;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenStoreService {

    AtomicReference<StoredToken> storedToken = new AtomicReference<>();

    public Optional<GatewayAccessToken> getToken() {
        StoredToken token = storedToken.get();
        if (token == null) {
            return Optional.empty();
        }
        if (!token.isValidAt(Instant.now())) {
            storedToken.compareAndSet(token, null);
            return Optional.empty();
        }
        return Optional.of(token.accessToken());
    }

    public void storeToken(GatewayAccessToken accessToken, Duration ttl) {
        Instant expiresAt = Instant.now().plus(ttl.isNegative() || ttl.isZero() ? Duration.ofSeconds(1) : ttl);
        storedToken.set(new StoredToken(accessToken, expiresAt));
    }

    public void evictToken() {
        storedToken.set(null);
    }

    private record StoredToken(
            GatewayAccessToken accessToken,
            Instant expiresAt
    ) {
        private boolean isValidAt(Instant now) {
            return expiresAt.isAfter(now);
        }
    }
}
