package uz.davrbank.payment.service.business.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.model.dto.billing.request.CheckRequest;
import uz.davrbank.payment.model.dto.billing.request.PayRequest;
import uz.davrbank.payment.model.dto.billing.response.BillingBaseResponse;
import uz.davrbank.payment.model.dto.billing.response.result.DataResult;
import uz.davrbank.payment.model.dto.billing.response.result.SRData;
import uz.davrbank.payment.model.dto.core.request.PayRequestDTO;
import uz.davrbank.payment.model.dto.core.response.PayResponseDTO;
import uz.davrbank.payment.model.entity.Bill;
import uz.davrbank.payment.model.entity.Transaction;
import uz.davrbank.payment.model.enums.CardType;
import uz.davrbank.payment.model.enums.PayType;
import uz.davrbank.payment.model.property.BillingProperty;
import uz.davrbank.payment.service.business.DebitCardService;
import uz.davrbank.payment.service.core.BillService;
import uz.davrbank.payment.service.core.TransactionService;
import uz.davrbank.payment.service.web.BillingClientService;
import uz.davrbank.payment.service.web.CardClientService;
import uz.davrbank.payment.utils.RequestTools;
import uz.davrbank.payment.utils.ServicesPermission;
import uz.davrbank.payment.utils.TransChecker;
import uz.nodir.commondto.exception.InvalidDataException;
import uz.davrbank.payment.model.dto.core.response.CardWithStatusDTO;
import uz.davrbank.payment.model.enums.CardStatus;
import uz.nodir.commondto.model.dto.core.response.ResultData;
import uz.nodir.commondto.model.enums.Processing;
import uz.nodir.commondto.model.enums.ResponseStatus;
import uz.nodir.commondto.model.enums.ResultState;
import uz.nodir.commondto.utils.CardUtils;
import uz.nodir.commondto.utils.SecurityUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DebitCardServiceImpl implements DebitCardService {
    private final BillingClientService billingClient;
    private final BillingProperty property;
    private final TransactionService transactionService;
    private final BillService billService;
    private final CardClientService cardClientService;

    @Override
    public ResultData<PayResponseDTO> pay(@NotNull PayRequestDTO payDTO) {
        log.info("Starting pay method with request: {}", payDTO);
        var internalId = UUID.randomUUID();

//        var serviceOptional = getServiceById(payDTO);
//
//        if (serviceOptional.isEmpty()) {
//            log.info("Service: {} not found", payDTO.getServiceId());
//            return new ResultData<>(ResultState.DataNotFound, ResultState.DataNotFound.getMessage());
//        }


        if (!TransChecker.isAdmin()) {
            var services = getServiceByIdV2(payDTO.getServiceId());

            if (services.isEmpty() || services.stream().noneMatch(s -> s.equals(payDTO.getServiceId())))
                return new ResultData<>(ResultState.BadRequest, "You don't have permission to this serviceId");
        }

        return doPay(payDTO, internalId);
    }

    private ResultData<PayResponseDTO> doPay(PayRequestDTO payDTO, UUID internalId) {
        var checkRequest = RequestTools.checkRequestBuilder(payDTO, internalId);

        var savedTransaction = prepareNewTransaction(payDTO);

        var savedBill = prepareNewBill(checkRequest, savedTransaction, internalId);

        var checkResponse = getCheckResponse(checkRequest);

        standardUpdateFields(savedTransaction, checkResponse, savedBill);

        if (checkResponse.getStatus() != ResponseStatus.CHECK_OK) {
            saveFailedTransaction(savedTransaction);
            return new ResultData<>(ResultState.FailedResponse, checkResponse.getResponseText());
        }


        var paymentId = savedTransaction.getPaymentId();

        var cardResponse = getCardResponse(payDTO);

        if (!cardResponse.getIsSuccess() && cardResponse.getResult() == null) {
            savedTransaction.setNote(cardResponse.getError().getMessage());
            saveFailedTransaction(savedTransaction);
            return new ResultData<>(ResultState.FailedResponse, cardResponse.getError().getMessage());
        }

        // Check card status - card must be active and not blocked
        var cardStatus = cardResponse.getResult().getStatus();
        if (cardStatus == null || !cardStatus.isActiveAndNotBlocked()) {
            String errorMessage = getCardStatusErrorMessage(cardStatus);
            log.warn("Payment rejected - card status: {}, message: {}", cardStatus, errorMessage);
            savedTransaction.setNote(errorMessage);
            saveFailedTransaction(savedTransaction);
            return new ResultData<>(ResultState.FailedResponse, errorMessage);
        }

        var payRequest = payRequestBuilder(cardResponse, paymentId);

        savedBill.setPayRequest(payRequest.toMap());

        var payResponse = getPayResponse(payRequest);

        savedBill.setPayResponse(payResponse.toMap());
        billService.save(savedBill);

        if (payResponse.getStatus() != ResponseStatus.SUCCESS) {
            handlePayFailedTransaction(savedTransaction, payResponse, savedBill);
            return new ResultData<>(ResultState.FailedResponse, payResponse.getResponseText());
        }

        savedTransaction.setStatus(ResponseStatus.SUCCESS);
        savedTransaction.setRrn(payResponse.getSender().getData().getExternalId());
        savedTransaction.setAccount(payResponse.getAccount());
        transactionService.save(savedTransaction);

        return new ResultData<>(
                new PayResponseDTO(
                        savedTransaction.getId(),
                        savedTransaction.getPaymentId(),
                        cardResponse.getResult().getMaskedPan(),
                        cardResponse.getResult().getFullName(),
                        cardResponse.getResult().getProcessing(),
                        payResponse.getSender().getData().getExternalId(),
                        payResponse.getAmount(),
                        payResponse.getRecipientShortName(),
                        payResponse.getAccount(),
                        savedTransaction.getStatus(),
                        savedTransaction.getCreatedTime(),
                        savedTransaction.getUpdatedTime(),
                        savedTransaction.getCreatedBy()
                ));
    }

    private BillingBaseResponse<DataResult<SRData>> getPayResponse(PayRequest payRequest) {
        try {
            return billingClient.pay(payRequest);
        } catch (Exception e) {
            return new BillingBaseResponse<>(e.getMessage(), ResponseStatus.S_NEED_CONFIRM);
        }
    }

    private ResultData<CardWithStatusDTO> getCardResponse(PayRequestDTO payDTO) {
        try {
            return cardClientService.getCardById(payDTO.getCard());
        } catch (Exception e) {
            log.error("Card feign client has error: {}", e.getMessage());
            return new ResultData<>(ResultState.FailedResponse, e.getMessage());
        }
    }

    private Optional<Map.Entry<String, String>> getServiceById(PayRequestDTO payDTO) {
        log.info("Search service: {} from Service List", payDTO.getServiceId());
        return property.getServices()
                .entrySet()
                .stream().filter(z ->
                        z.getValue().equals(payDTO.getServiceId())).findFirst();
    }

    private List<String> getServiceByIdV2(String serviceId) {
        log.info("Search service: {} from Service Permission list", serviceId);
        var services = ServicesPermission.getUserService(SecurityUtils.getCurrentUserName());

        log.info("Services list: {}", services);
        if (services == null)
            return List.of();

        return services;
    }

    private void handlePayFailedTransaction(Transaction savedTransaction, BillingBaseResponse<DataResult<SRData>> payResponse, Bill savedBill) {
        savedTransaction.setNote(payResponse.getResponseText());
        savedTransaction.setStatus(payResponse.getStatus());
        transactionService.save(savedTransaction);

        savedBill.setPayResponse(payResponse.toMap());
        billService.save(savedBill);
    }

    private void standardUpdateFields(Transaction savedTransaction, BillingBaseResponse<?> checkResponse, Bill savedBill) {
        savedTransaction.setNote(checkResponse.getResponseText());
        savedTransaction.setPaymentId(String.valueOf(checkResponse.getId()));

        savedBill.setCheckResponse(checkResponse.toMap());
        billService.save(savedBill);
    }

    private static PayRequest payRequestBuilder(ResultData<CardWithStatusDTO> cardResponse, String paymentId) {
        Map<String, String> sentParams = new HashMap<>();

        if (cardResponse.getResult().getProcessing() == Processing.HUMO)
            sentParams.put(CardType.pan.toString(), CardUtils.decryptPan(cardResponse.getResult().getCard()));
        else
            sentParams.put(CardType.token.toString(), cardResponse.getResult().getCard());

        return new PayRequest(
                paymentId,
                PayType.PC,
                sentParams
        );
    }

    private BillingBaseResponse<?> getCheckResponse(CheckRequest checkRequest) {
        try {
            return billingClient.check(checkRequest);
        } catch (Exception e) {
            log.error("Billing check response has error: {}", e.getMessage());
            return new BillingBaseResponse<>(e.getMessage(), ResponseStatus.CHECK_FAIL);
        }
    }

    private Bill prepareNewBill(CheckRequest checkRequest, Transaction savedTransaction, UUID internalId) {
        var bill = new Bill(
                checkRequest,
                savedTransaction.getId(),
                internalId.toString());
        return billService.save(bill);
    }

    private Transaction prepareNewTransaction(PayRequestDTO payDTO) {
        var transaction = new Transaction(
                payDTO.getId(),
                payDTO.getAmount(),
                payDTO.getAccount(),
                ResponseStatus.CREATED
        );

        try {
            return transactionService.save(transaction);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException(NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        }
    }


    private void saveFailedTransaction(Transaction transaction) {
        transaction.setStatus(ResponseStatus.CHECK_FAIL);
        transactionService.save(transaction);
    }

    private String getCardStatusErrorMessage(CardStatus status) {
        if (status == null) {
            return "Card status is unknown";
        }
        return switch (status) {
            case HARD_BLOCKED -> "Card is blocked";
            case SOFT_BLOCKED -> "Card is temporarily blocked";
            case INACTIVE -> "Card is inactive";
            case SERVICE_UNAVAILABLE -> "Card service is unavailable";
            case PENDING -> "Card is pending activation";
            default -> "Card is not active";
        };
    }

}
