package uz.davrbank.userinfobase.gateway;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import uz.davrbank.userinfobase.client.DavrhubAccessToken;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DavrhubTokenStoreService {

    AtomicReference<StoredToken> storedToken = new AtomicReference<>();

    public Optional<DavrhubAccessToken> getToken() {
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

    public void storeToken(DavrhubAccessToken accessToken, Duration ttl) {
        Duration effectiveTtl = ttl.isNegative() || ttl.isZero() ? Duration.ofSeconds(1) : ttl;
        storedToken.set(new StoredToken(accessToken, Instant.now().plus(effectiveTtl)));
    }

    public void evictToken() {
        storedToken.set(null);
    }

    private record StoredToken(
            DavrhubAccessToken accessToken,
            Instant expiresAt
    ) {
        private boolean isValidAt(Instant now) {
            return expiresAt.isAfter(now);
        }
    }
}