package uz.davrbank.partnervalidationservice.validation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import uz.davrbank.partnervalidationservice.client.GatewayClient;
import uz.davrbank.partnervalidationservice.logging.enums.MonitoringStep;
import uz.davrbank.partnervalidationservice.logging.service.AuditLogService;
import uz.davrbank.partnervalidationservice.merchant.entity.Merchant;
import uz.davrbank.partnervalidationservice.merchant.entity.MerchantAccount;
import uz.davrbank.partnervalidationservice.merchant.enums.AccountStatus;
import uz.davrbank.partnervalidationservice.merchant.enums.MerchantStatus;
import uz.davrbank.partnervalidationservice.merchant.repository.MerchantAccountRepository;
import uz.davrbank.partnervalidationservice.merchant.repository.MerchantRepository;
import uz.davrbank.partnervalidationservice.validation.dto.*;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PartnerValidationService {

    static final String DAVRHUB_METHOD_105_URI = "/api/v1/davrhub/execute/list/105";
    static final String DAVRHUB_METHOD_202_URI = "/api/v1/davrhub/execute/list/202";
    private static final String POST_METHOD = HttpMethod.POST.name();
    private static final String GATEWAY_ERROR_MESSAGE = "Gateway error or response is empty";

    MerchantRepository merchantRepository;
    MerchantAccountRepository merchantAccountRepository;
    AuditLogService auditLogService;
    GatewayClient gatewayClient;

    @Transactional
    public ValidationResponse validate(ValidationRequest request) {
        UUID correlationId = UUID.randomUUID();
        logIncomingRequest(request, correlationId);

        ValidationResponse requestValidationError = validateRequestStructure(correlationId, request);
        if (requestValidationError != null) {
            return requestValidationError;
        }

        Merchant merchant = findMerchant(request);
        if (merchant == null) {
            return failure(
                    correlationId,
                    MonitoringStep.MERCHANT_NOT_EXISTS,
                    request,
                    null,
                    null,
                    MonitoringStep.MERCHANT_NOT_EXISTS.name(),
                    "Merchant not found"
            );
        }

        ValidationResponse merchantStatusValidationError = validateMerchantStatus(correlationId, request, merchant);
        if (merchantStatusValidationError != null) {
            return merchantStatusValidationError;
        }

        MerchantAccount merchantAccount = findMerchantAccount(merchant, request);
        if (merchantAccount == null) {
            boolean accountExistsForAnotherMerchant = merchantAccountRepository.existsByAccountNumber(request.payload().payerAccount());
            MonitoringStep step = accountExistsForAnotherMerchant
                    ? MonitoringStep.ACCOUNT_NUMBER_NOT_BELONG_TO_MERCHANT
                    : MonitoringStep.MERCHANT_ACCOUNT_NOT_EXISTS;
            String errorMessage = accountExistsForAnotherMerchant
                    ? "Merchant account does not belong to merchant"
                    : "Merchant account not found";
            return failure(
                    correlationId,
                    step,
                    request,
                    null,
                    null,
                    step.name(),
                    errorMessage
            );
        }

        ValidationResponse merchantAccountStatusValidationError = validateMerchantAccountStatus(correlationId, request, merchantAccount);
        if (merchantAccountStatusValidationError != null) {
            return merchantAccountStatusValidationError;
        }

        ValidationResponse merchantAccountValidationError = validateMerchantAccount(correlationId, request, merchant, merchantAccount);
        if (merchantAccountValidationError != null) {
            return merchantAccountValidationError;
        }

        ValidationResponse merchantValidationError = validateMerchant(correlationId, request, merchant);
        if (merchantValidationError != null) {
            return merchantValidationError;
        }

        return new ValidationResponse(true, request.payload(), null, null);
    }

    private void logIncomingRequest(ValidationRequest request, UUID correlationId) {
        auditLogService.log(MonitoringStep.ROW_INCOMING_REQUEST, request, null, POST_METHOD, null, true, correlationId);
    }

    private ValidationResponse validateRequestStructure(UUID correlationId, ValidationRequest request) {
        if (request == null) {
            return illegalRequest(correlationId, request, "Request must not be null");
        }
        if (request.payload() == null) {
            return illegalRequest(correlationId, request, "Request payload must not be null");
        }
        if (!StringUtils.hasText(request.merchantId())) {
            return illegalRequest(correlationId, request, "Request merchant id must not be blank");
        }
        if (!StringUtils.hasText(request.payload().payerAccount())) {
            return illegalRequest(correlationId, request, "Request payer account must not be blank");
        }
        if (!StringUtils.hasText(request.payload().branch())) {
            return illegalRequest(correlationId, request, "Request branch must not be blank");
        }
        return null;
    }

    private Merchant findMerchant(ValidationRequest request) {
        Optional<Merchant> merchant = merchantRepository.findFirstByMerchantExternalId(request.merchantId());
        return merchant.orElse(null);
    }

    private MerchantAccount findMerchantAccount(Merchant merchant, ValidationRequest request) {
        Optional<MerchantAccount> merchantAccount =
                merchantAccountRepository.findFirstByMerchantIdAndAccountNumber(merchant.getId(), request.payload().payerAccount());
        return merchantAccount.orElse(null);
    }

    private ValidationResponse validateMerchantStatus(UUID correlationId, ValidationRequest request, Merchant merchant) {
        if (merchant.getStatus() == MerchantStatus.ACTIVE) {
            return null;
        }
        return failure(
                correlationId,
                MonitoringStep.MERCHANT_BLOCKED,
                request,
                null,
                null,
                MonitoringStep.MERCHANT_BLOCKED.name(),
                "Merchant is blocked"
        );
    }

    private ValidationResponse validateMerchantAccountStatus(
            UUID correlationId,
            ValidationRequest request,
            MerchantAccount merchantAccount
    ) {
        if (merchantAccount.getStatus() == AccountStatus.ACTIVE) {
            return null;
        }
        return failure(
                correlationId,
                MonitoringStep.MERCHANT_ACCOUNT_NOT_ACTIVE,
                request,
                null,
                null,
                MonitoringStep.MERCHANT_ACCOUNT_NOT_ACTIVE.name(),
                "Merchant account is blocked"
        );
    }

    private ValidationResponse validateMerchantAccount(
            UUID correlationId,
            ValidationRequest request,
            Merchant merchant,
            MerchantAccount merchantAccount
    ) {
        try {
            GatewayMethod202Response response = fetchGatewayMethod202Response(request, merchant);
            ValidationResponse gatewayError = validateGatewayMethod202Response(correlationId, request, response);
            if (gatewayError != null) {
                return gatewayError;
            }
            if (!StringUtils.hasText(response.id())) {
                return failure(
                        correlationId,
                        MonitoringStep.GATEWAY_202_METHOD_ERROR,
                        request,
                        response,
                        DAVRHUB_METHOD_202_URI,
                        MonitoringStep.GATEWAY_202_METHOD_ERROR.name(),
                        "Gateway response does not contain merchant account identifier"
                );
            }
            if (StringUtils.hasText(merchantAccount.getAbsClientId())
                    && !Objects.equals(merchantAccount.getAbsClientId(), response.id())) {
                return failure(
                        correlationId,
                        MonitoringStep.MERCHANT_ACCOUNT_NUMBER_MISMATCH,
                        request,
                        response,
                        DAVRHUB_METHOD_202_URI,
                        MonitoringStep.MERCHANT_ACCOUNT_NUMBER_MISMATCH.name(),
                        "Merchant account identifier mismatch"
                );
            }
            if (!StringUtils.hasText(merchantAccount.getAbsClientId())) {
                merchantAccount.setAbsClientId(response.id());
                merchantAccountRepository.save(merchantAccount);
            }

            if (!Objects.equals(response.state(), 2)) {
                return failure(
                        correlationId,
                        MonitoringStep.MERCHANT_ACCOUNT_NOT_ACTIVE,
                        request,
                        response,
                        DAVRHUB_METHOD_202_URI,
                        MonitoringStep.MERCHANT_ACCOUNT_NOT_ACTIVE.name(),
                        "Merchant account is not active"
                );
            }
            if (response.dateClose() != null) {
                return failure(
                        correlationId,
                        MonitoringStep.MERCHANT_ACCOUNT_CLOSED,
                        request,
                        response,
                        DAVRHUB_METHOD_202_URI,
                        MonitoringStep.MERCHANT_ACCOUNT_CLOSED.name(),
                        "Merchant account is closed"
                );
            }
            if (!Objects.equals(response.branch(), merchantAccount.getBranchCode())) {
                return failure(
                        correlationId,
                        MonitoringStep.BRANCH_ACCOUNT_CHECK_FAILED,
                        request,
                        response,
                        DAVRHUB_METHOD_202_URI,
                        MonitoringStep.BRANCH_ACCOUNT_CHECK_FAILED.name(),
                        "Merchant account branch mismatch"
                );
            }
            return null;
        } catch (Exception exception) {
            return gatewayExceptionFailure(
                    correlationId,
                    request,
                    MonitoringStep.GATEWAY_202_METHOD_ERROR,
                    DAVRHUB_METHOD_202_URI,
                    exception
            );
        }
    }

    private GatewayMethod202Response fetchGatewayMethod202Response(ValidationRequest request, Merchant merchant) {
        GatewayMethod202Request gatewayMethod202Request = new GatewayMethod202Request(
                request.payload().payerAccount(),
                merchant.getName(),
                request.merchantId(),
                request.payload().payerAccount(),
                null,
                null,
                null,
                request.payload().branch()
        );
        Mono<GatewayMethod202Response> response =
                gatewayClient.exchange(HttpMethod.POST, DAVRHUB_METHOD_202_URI, gatewayMethod202Request,
                        GatewayMethod202Response.class);
        return response.block();
    }

    private ValidationResponse validateGatewayMethod202Response(
            UUID correlationId,
            ValidationRequest request,
            GatewayMethod202Response response
    ) {
        if (response == null || response.errorCode() != null) {
            String errorMessage = response == null ? GATEWAY_ERROR_MESSAGE : response.errorNote();
            return failure(
                    correlationId,
                    MonitoringStep.GATEWAY_202_METHOD_ERROR,
                    request,
                    response,
                    DAVRHUB_METHOD_202_URI,
                    MonitoringStep.GATEWAY_202_METHOD_ERROR.name(),
                    errorMessage
            );
        }
        return null;
    }

    private ValidationResponse validateMerchant(UUID correlationId, ValidationRequest request, Merchant merchant) {
        try {
            GatewayMethod105Response response = fetchGatewayMethod105Response(request, merchant);
            ValidationResponse gatewayError = validateGatewayMethod105Response(correlationId, request, response);
            if (gatewayError != null) {
                return gatewayError;
            }
            if (response.state() != null && !Objects.equals(response.state(), 2)) {
                return blockedMerchantFailure(correlationId, request, response);
            }
            if (response.dateClose() != null) {
                return failure(
                        correlationId,
                        MonitoringStep.MERCHANT_ACCOUNT_CLOSED,
                        request,
                        response,
                        DAVRHUB_METHOD_105_URI,
                        MonitoringStep.MERCHANT_ACCOUNT_CLOSED.name(),
                        "Merchant is closed"
                );
            }
            if (!Objects.equals(response.inps(), merchant.getTin())) {
                return failure(correlationId,
                        MonitoringStep.MERCHANT_INPS_MISMATCH,
                        request,
                        response,
                        DAVRHUB_METHOD_105_URI,
                        MonitoringStep.MERCHANT_INPS_MISMATCH.name(),
                        "Merchant INPS mismatch"
                );
            }
            if (response.blackCode() != null && !Objects.equals(response.blackCode(), 0)) {
                return blacklistedMerchantFailure(correlationId, request, response);
            }
            if (response.typeBlack() != null && !Objects.equals(response.typeBlack(), 0)) {
                return blacklistedMerchantFailure(correlationId, request, response);
            }
            return null;
        } catch (Exception exception) {
            return gatewayExceptionFailure(
                    correlationId,
                    request,
                    MonitoringStep.GATEWAY_105_METHOD_ERROR,
                    DAVRHUB_METHOD_105_URI,
                    exception
            );
        }
    }

    private ValidationResponse blockedMerchantFailure(
            UUID correlationId,
            ValidationRequest request,
            GatewayMethod105Response response
    ) {
        return failure(
                correlationId,
                MonitoringStep.MERCHANT_BLOCKED,
                request,
                response,
                DAVRHUB_METHOD_105_URI,
                MonitoringStep.MERCHANT_BLOCKED.name(),
                "Merchant is blocked"
        );
    }

    private GatewayMethod105Response fetchGatewayMethod105Response(ValidationRequest request, Merchant merchant) {
        GatewayMethod105Request gatewayMethod105Request =
                new GatewayMethod105Request(merchant.getTin(), request.payload().branch());
        Mono<GatewayMethod105Response> response =
                gatewayClient.exchange(HttpMethod.POST, DAVRHUB_METHOD_105_URI, gatewayMethod105Request,
                        GatewayMethod105Response.class);
        return response.block();
    }

    private ValidationResponse validateGatewayMethod105Response(
            UUID correlationId,
            ValidationRequest request,
            GatewayMethod105Response response
    ) {
        if (response == null || response.errorCode() != null) {
            String errorMessage = response == null ? GATEWAY_ERROR_MESSAGE : response.errorNote();
            return failure(
                    correlationId,
                    MonitoringStep.GATEWAY_105_METHOD_ERROR,
                    request,
                    response,
                    DAVRHUB_METHOD_105_URI,
                    MonitoringStep.GATEWAY_105_METHOD_ERROR.name(),
                    errorMessage
            );
        }
        return null;
    }

    private ValidationResponse blacklistedMerchantFailure(
            UUID correlationId,
            ValidationRequest request,
            GatewayMethod105Response response
    ) {
        return failure(
                correlationId,
                MonitoringStep.MERCHANT_IN_BLACKLIST,
                request,
                response,
                DAVRHUB_METHOD_105_URI,
                MonitoringStep.MERCHANT_IN_BLACKLIST.name(),
                "Merchant is blacklisted"
        );
    }

    private ValidationResponse illegalRequest(UUID correlationId, ValidationRequest request, String errorMessage) {
        return failure(
                correlationId,
                MonitoringStep.ILLEGAL_REQUEST,
                request,
                null,
                null,
                MonitoringStep.ILLEGAL_REQUEST.name(),
                errorMessage
        );
    }

    private ValidationResponse gatewayExceptionFailure(
            UUID correlationId,
            ValidationRequest request,
            MonitoringStep monitoringStep,
            String url,
            Exception exception
    ) {
        String errorMessage = exception.getMessage() == null ? GATEWAY_ERROR_MESSAGE : exception.getMessage();
        return failure(
                correlationId,
                monitoringStep,
                request,
                errorMessage,
                url,
                monitoringStep.name(),
                errorMessage
        );
    }

    private ValidationResponse failure(
            UUID correlationId,
            MonitoringStep monitoringStep,
            ValidationRequest request,
            Object responsePayload,
            String url,
            String errorCode,
            String errorMessage
    ) {
        auditLogService.log(monitoringStep, request, responsePayload, POST_METHOD, url, false, correlationId);
        return new ValidationResponse(false, null, errorCode, errorMessage);
    }
}
