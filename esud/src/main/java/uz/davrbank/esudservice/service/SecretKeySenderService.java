package uz.davrbank.esudservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.esudservice.aggregate.dto.response.BaseResponse;
import uz.davrbank.esudservice.aggregate.dto.response.SecretKeyResponse;
import uz.davrbank.esudservice.aggregate.dto.reuqest.SecretKeyRequest;
import uz.davrbank.esudservice.aggregate.feign.CourtIntegrationFeign;
import uz.davrbank.esudservice.service.crypto.CryptoService;
import uz.davrbank.esudservice.service.crypto.RsaAesHybridEncryptor;
import uz.davrbank.esudservice.service.crypto.dto.CertificateWithKeyPair;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Service responsible for securely sending a secret AES-256 session key to the court system.
 * <p>
 * The service performs the following steps:
 * <ol>
 *     <li>Loads the court's X.509 certificate from a PFX file and retrieves the public key.</li>
 *     <li>Generates a new AES-256 session key.</li>
 *     <li>Encrypts the AES key using RSA with the court's public key.</li>
 *     <li>Encodes the encrypted key in Base64 and sends it to the court via a Feign client.</li>
 * </ol>
 * </p>
 * <p>
 * The method {@link #sendSecretKey(String, String)} is protected with
 * <b>Resilience4j Retry</b> and <b>CircuitBreaker</b> mechanisms.
 * In case of failure, the fallback method {@link #fallbackSendSecretKey(SecretKeyRequest, Throwable)}
 * is invoked, which can persist the request for later retry.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SecretKeySenderService {

    private final CryptoService cryptoService;
    private final CourtIntegrationFeign judgeFeign;

    /**
     * Sends a secret AES-256 key to the court system.
     * <p>
     * Steps performed:
     * <ul>
     *     <li>Load the court certificate from the specified PFX path.</li>
     *     <li>Generate a new AES-256 session key.</li>
     *     <li>Encrypt the AES key with the court's RSA public key.</li>
     *     <li>Send the encrypted key to the court using Feign client.</li>
     * </ul>
     *
     * @param pfxPath  the file path to the PFX certificate containing the court's public key
     * @param password the password for the PFX certificate
     * @return {@link SecretKeyResponse} containing status and message from the court system
     * @throws Exception if certificate loading, key generation, or encryption fails
     */
    @SneakyThrows
    @Retry(name = "secretKeySenderService", fallbackMethod = "fallbackSendSecretKey")
    @CircuitBreaker(name = "secretKeySenderService", fallbackMethod = "fallbackSendSecretKey")
    public BaseResponse<Void> sendSecretKey(String pfxPath, String password) {

        // 1) Load certificate and public key
        CertificateWithKeyPair cert = cryptoService.loadFromPfx(pfxPath, password);
        PublicKey courtPublicKey = cert.keyPair().getPublic();

        // 2) Generate AES-256 session key
        SecretKey aesKey = RsaAesHybridEncryptor.genAESKey();

        // 3) Encrypt AES key with RSA
        byte[] encryptedKeyBytes = RsaAesHybridEncryptor.rsaEncryptKey(aesKey, courtPublicKey);
        String encryptedKeyBase64 = Base64.getEncoder().encodeToString(encryptedKeyBytes);

        // 4) Prepare request
        SecretKeyRequest request = new SecretKeyRequest(encryptedKeyBase64);

        // 5) Send to court system
        return judgeFeign.sendSecretKey(request);
    }

    /**
     * Fallback method invoked when sending the secret key fails due to service unavailability or errors.
     * <p>
     * The failed request can be persisted to a queue or database for later retry.
     * </p>
     *
     * @param request the original {@link SecretKeyRequest} that failed to send
     * @param t       the {@link Throwable} that caused the failure
     * @return {@link SecretKeyResponse} indicating failure
     */
    public BaseResponse<Void>  fallbackSendSecretKey(SecretKeyRequest request, Throwable t) {
        log.error("Failed to send secret key: {}", t.getMessage());
        // Persist request to queue or database if needed

        BaseResponse<Void> baseResponse= new SecretKeyResponse();
        baseResponse.setMessage("Failed to send secret key");
        baseResponse.setTimestamp(LocalDateTime.now());
        return baseResponse;
    }
}
