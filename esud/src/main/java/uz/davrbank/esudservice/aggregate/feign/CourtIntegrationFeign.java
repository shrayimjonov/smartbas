package uz.davrbank.esudservice.aggregate.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.davrbank.esudservice.aggregate.dto.response.BaseResponse;
import uz.davrbank.esudservice.aggregate.dto.response.SecretKeyResponse;
import uz.davrbank.esudservice.aggregate.dto.reuqest.RegisterCaseRequestDto;
import uz.davrbank.esudservice.aggregate.dto.reuqest.SecretKeyRequest;
import uz.davrbank.esudservice.dto.out.ResponseData;

@FeignClient(value = "court-service", url = "${integration-court.server.url}", configuration = FeignConfig.class)
public interface CourtIntegrationFeign {

    /**
     * Registers a new claim case in the external Case Management Service.
     *
     * <p>This endpoint creates a new claim request based on the provided
     * {@link RegisterCaseRequestDto}. Input DTO is validated using Bean Validation
     * annotations. Any validation errors will throw an exception before the Feign call.</p>
     *
     * @param request the payload containing claim registration details. Must not be {@code null}.
     * @return a {@link BaseResponse} containing {@link ResponseData} returned by CMS.
     * @throws jakarta.validation.ConstraintViolationException if the request DTO fails validation
     * @throws feign.FeignException                            if the external service returns an error HTTP status
     */
    @PostMapping("/api/v1/cases/register-case")
    BaseResponse<ResponseData>  registerCase(
            @Valid @RequestBody RegisterCaseRequestDto request
    );

    /**
     * Sends a secret AES-256 session key to the court system.
     *
     * <p>The AES key is encrypted with the court's public key (from a PFX certificate)
     * and sent via a secure channel. The request body contains the Base64-encoded
     * encrypted key.</p>
     *
     * <p><b>Example JSON request:</b></p>
     * <pre>
     * {
     *   "encryptedKey": "Base64EncodedEncryptedAESKey"
     * }
     * </pre>
     *
     * <p><b>Example JSON response:</b></p>
     * <pre>
     * {
     *   "message": "Secret key successfully registered with the court system",
     *   "timestamp": "2025-11-18T11:00:00"
     * }
     * </pre>
     *
     * @param request the {@link SecretKeyRequest} containing the Base64-encoded AES session key.
     * @return a {@link BaseResponse} containing {@link SecretKeyResponse} indicating success or failure.
     */
    @PostMapping("/api/v1/crypto/save-secret-key")
    BaseResponse<Void> sendSecretKey(@Valid @RequestBody SecretKeyRequest request);

}
