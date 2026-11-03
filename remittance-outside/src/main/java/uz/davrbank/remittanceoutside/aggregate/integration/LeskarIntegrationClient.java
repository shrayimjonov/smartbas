/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.aggregate.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uz.davrbank.remittanceoutside.aggregate.configuration.ApplicationProperties;
import uz.davrbank.remittanceoutside.aggregate.dto.leskarintegration.LeskarEncryptedDataPayload;
import uz.davrbank.remittanceoutside.aggregate.dto.leskarintegration.LeskarLoginRequest;
import uz.davrbank.remittanceoutside.aggregate.dto.leskarintegration.LeskarLoginResponse;
import uz.davrbank.remittanceoutside.aggregate.dto.leskarintegration.base.LeskarBaseRequest;
import uz.davrbank.remittanceoutside.aggregate.dto.leskarintegration.base.LeskarBaseResponse;
import uz.davrbank.remittanceoutside.aggregate.enums.IntegrationType;
import uz.davrbank.remittanceoutside.exception.condition.IntegrationException;
import uz.davrbank.remittanceoutside.aggregate.external.Connector;
import uz.davrbank.remittanceoutside.aggregate.integration.base.EncryptedIntegrationClient;
import uz.davrbank.remittanceoutside.aggregate.integration.base.TokenBasedIntegrationClient;
import uz.davrbank.remittanceoutside.service.TokenStoreService;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;

@Component
public class LeskarIntegrationClient implements TokenBasedIntegrationClient, EncryptedIntegrationClient {

    Connector connector;
    ApplicationProperties applicationProperties;
    ObjectMapper objectMapper;
    TokenStoreService tokenStoreService;
    private static final SecureRandom random = new SecureRandom();

    public LeskarIntegrationClient(Connector connector, ApplicationProperties applicationProperties, @Qualifier("leskarIntegrationObjectMapper") ObjectMapper objectMapper,TokenStoreService tokenStoreService) {
        this.connector = connector;
        this.applicationProperties = applicationProperties;
        this.objectMapper = objectMapper;
        this.tokenStoreService = tokenStoreService;
    }

    @Override
    public String getChannelKey() {
        return IntegrationType.LESKAR.getName();
    }

    @Override
    public <REQUEST, RESPONSE> RESPONSE post(String path, REQUEST body, Map<String, ?> params, ParameterizedTypeReference<RESPONSE> responseType, HttpHeaders headers) {
        ResponseEntity<RESPONSE> res = connector.exchange(
                getChannelKey(),
                path,
                HttpMethod.POST,
                params,
                getHeaders(headers),
                getTimeout(),
                wrapRequest(body),
                responseType
        );
        if (res == null || !res.getStatusCode().is2xxSuccessful()) {
            throw new IntegrationException("Leskar response error: path=" + path + " status=" + (res != null ? res.getStatusCode() : "null"));
        }
        RESPONSE bodyRes = res.getBody();
        if (bodyRes instanceof LeskarBaseResponse<?> leskarResp) {
            verifyResponseSignature(leskarResp);
        }
        return bodyRes;
    }

    @Override
    public <RESPONSE> RESPONSE get(String path, Map<String, ?> params, ParameterizedTypeReference<RESPONSE> responseType, HttpHeaders headers) {
        ResponseEntity<RESPONSE> res = connector.exchange(
                getChannelKey(),
                path,
                HttpMethod.GET,
                params,
                getHeaders(headers),
                getTimeout(),
                null,
                responseType
        );
        if (res == null || !res.getStatusCode().is2xxSuccessful()) {
            throw new IntegrationException("Leskar response error: path=" + path + " status=" + (res != null ? res.getStatusCode() : "null"));
        }
        RESPONSE bodyRes = res.getBody();
        if (bodyRes instanceof LeskarBaseResponse<?> leskarResp) {
            verifyResponseSignature(leskarResp);
        }
        return bodyRes;
    }

    @Override
    public String getToken() {
        String token = tokenStoreService.getToken(IntegrationType.LESKAR);
        if (StringUtils.hasText(token)) {
            return token;
        }
        final String path = "/v1/transfer/agent/auth";

        ResponseEntity<LeskarBaseResponse<LeskarLoginResponse>> res = connector.exchange(
                getChannelKey(),
                path,
                HttpMethod.POST,
                null,
                new HttpHeaders(),
                getTimeout(),
                new LeskarLoginRequest(applicationProperties.getLeskar().getAgentSecret(), applicationProperties.getLeskar().getAgentId()),
                new ParameterizedTypeReference<>() {
                }
        );

        if (res == null || !res.getStatusCode().is2xxSuccessful()) {
            throw new IntegrationException("Leskar token call error: path=" + path + " status=" + (res != null ? res.getStatusCode() : "null"));
        }

        LeskarBaseResponse<LeskarLoginResponse> body = res.getBody();
        if (body == null || body.getPayload() == null || !StringUtils.hasText(body.getPayload().token())) {
            throw new IntegrationException("Leskar token is null");
        }

        token = body.getPayload().token();
        tokenStoreService.setToken(IntegrationType.LESKAR, token);
        return token;
    }


    @Override
    public HttpHeaders getHeaders(HttpHeaders headers) {
        HttpHeaders httpHeaders = TokenBasedIntegrationClient.super.getHeaders(headers);
        httpHeaders.set("Authorization", "Bearer " + getToken());
        return httpHeaders;
    }

    @Override
    public Duration getTimeout() {
        return Duration.ofSeconds(30);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <REQUEST, ENCRYPTED_OBJECT> ENCRYPTED_OBJECT encrypt(REQUEST request) {
        if (request == null) {
            return null;
        }
        ObjectMapper mapper = objectMapper;
        try {
            String canonicalJson = mapper.writeValueAsString(request);

            String signatureHex = getSignature(canonicalJson);

            byte[] iv = new byte[16];
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sha256(applicationProperties.getLeskar().getPartnerKey().getBytes(StandardCharsets.UTF_8)), "AES"), new IvParameterSpec(iv));
            byte[] ct = cipher.doFinal(canonicalJson.getBytes(StandardCharsets.UTF_8));
            ByteBuffer out = ByteBuffer.allocate(iv.length + ct.length);
            out.put(iv).put(ct);
            return (ENCRYPTED_OBJECT) (wrapRequest(signatureHex, Base64.getEncoder().encodeToString(out.array())));

        } catch (Exception e) {
            throw new IntegrationException("Encryption failed", e);
        }
    }

    @Override
    public <REQUEST, RESPONSE> RESPONSE postEncrypted(String path, REQUEST body, Map<String, ?> params, ParameterizedTypeReference<RESPONSE> responseType, HttpHeaders headers) {
        ResponseEntity<RESPONSE> res = connector.exchange(
                getChannelKey(),
                path,
                HttpMethod.POST,
                params,
                getHeaders(headers),
                getTimeout(),
                encrypt(body),
                responseType
        );
        if (res == null || !res.getStatusCode().is2xxSuccessful()) {
            throw new IntegrationException("Leskar response error: path=" + path + " status=" + (res != null ? res.getStatusCode() : "null"));
        }
        RESPONSE bodyRes = res.getBody();
        if (bodyRes instanceof LeskarBaseResponse<?> leskarResp) {
            verifyResponseSignature(leskarResp);
        }
        return bodyRes;
    }

    private static String hmacSha256Hex(byte[] data, byte[] key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return toHexLower(mac.doFinal(data));
    }

    private static byte[] sha256(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(bytes);
        } catch (Exception e) {
            throw new IllegalStateException("Encryption failed", e);
        }
    }

    private static String toHexLower(byte[] bytes) {
        return HexFormat.of().formatHex(bytes);
    }

    private String getSignature(String json) {
        try {
            return hmacSha256Hex(json.getBytes(StandardCharsets.UTF_8), applicationProperties.getLeskar().getAgentSecret().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private LeskarBaseRequest<LeskarEncryptedDataPayload> wrapRequest(String signatureHex, String encryptedData) {
        return new LeskarBaseRequest<>(signatureHex, new LeskarEncryptedDataPayload(encryptedData));
    }

    private <REQUEST> LeskarBaseRequest<REQUEST> wrapRequest(REQUEST request) {
        try {
            return new LeskarBaseRequest<>(getSignature(objectMapper.writeValueAsString(request)), request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyResponseSignature(LeskarBaseResponse<?> response) {
        if (response == null) {
            throw new IntegrationException("Response is null");
        }
        String signature = response.getSignature();
        try {
            String payloadJson = objectMapper.writeValueAsString(response.getPayload());
            String expected = getSignature(payloadJson);
            if (!expected.equalsIgnoreCase(signature)) {
                throw new IntegrationException("Leskar response signature verification failed");
            }
        } catch (JsonProcessingException e) {
            throw new IntegrationException("Failed to serialize response payload for signature verification", e);
        }
    }
}