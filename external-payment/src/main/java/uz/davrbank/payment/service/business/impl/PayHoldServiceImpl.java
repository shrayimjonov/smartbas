package uz.davrbank.payment.service.business.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.model.dto.billing.request.CancelRequest;
import uz.davrbank.payment.model.dto.billing.request.CheckRequest;
import uz.davrbank.payment.model.dto.billing.request.HoldRequest;
import uz.davrbank.payment.model.dto.billing.response.BillingBaseResponse;
import uz.davrbank.payment.model.dto.billing.response.result.DataResult;
import uz.davrbank.payment.model.dto.billing.response.result.SRData;
import uz.davrbank.payment.model.dto.core.request.PayRequestDTO;
import uz.davrbank.payment.model.dto.core.response.HoldCancelResponseDTO;
import uz.davrbank.payment.model.dto.core.response.PayResponseDTO;
import uz.davrbank.payment.model.entity.Bill;
import uz.davrbank.payment.model.entity.Transaction;
import uz.davrbank.payment.model.enums.CardType;
import uz.davrbank.payment.model.enums.PayType;
import uz.davrbank.payment.model.property.BillingProperty;
import uz.davrbank.payment.service.business.PayHoldService;
import uz.davrbank.payment.service.core.BillService;
import uz.davrbank.payment.service.core.TransactionService;
import uz.davrbank.payment.service.web.BillingClientService;
import uz.davrbank.payment.service.web.CardClientService;
import uz.davrbank.payment.utils.RequestTools;
import uz.davrbank.payment.utils.ServicesPermission;
import uz.davrbank.payment.utils.TransChecker;
import uz.nodir.commondto.exception.EntityNotFoundException;
import uz.nodir.commondto.exception.GeneralErrorException;
import uz.nodir.commondto.exception.InvalidDataException;
import uz.davrbank.payment.model.dto.core.response.CardWithStatusDTO;
import uz.davrbank.payment.model.enums.CardStatus;
import uz.nodir.commondto.model.dto.core.response.ResultData;
import uz.nodir.commondto.model.enums.Processing;
import uz.nodir.commondto.model.enums.ResponseStatus;
import uz.nodir.commondto.model.enums.ResultState;
import uz.nodir.commondto.utils.SecurityUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PayHoldServiceImpl implements PayHoldService {
    private final BillingProperty property;
    private final TransactionService transactionService;
    private final BillService billService;
    private final BillingClientService billingClient;
    private final CardClientService cardClientService;

    @Override
    public ResultData<PayResponseDTO> holdActive(@NotNull PayRequestDTO payDTO) {
        log.info("Starting hold active with request: {}", payDTO);

        checkServiceByUser(payDTO.getServiceId());

        return doHold(payDTO);
    }

    private void checkServiceByUser(String serviceId) {
        if (TransChecker.isAdmin()) {
            log.info("Admin user bypassing service permission check for serviceId: {}", serviceId);
            return;
        }

        log.info("Search service: {} from Service Permission list", serviceId);
        var services = ServicesPermission.getUserService(SecurityUtils.getCurrentUserName());

        log.info("Services list: {}", services);

        if (services == null || services.isEmpty())
            throw new EntityNotFoundException("Any service not found");

        if (!services.contains(serviceId))
            throw new EntityNotFoundException("ServiceId: %s not permitted".formatted(serviceId));
    }

    private void getServiceById(PayRequestDTO payDTO) {
        log.info("Search service: {} from Service List", payDTO.getServiceId());
        property.getServices()
                .entrySet()
                .stream().filter(z ->
                        z.getValue().equals(payDTO.getServiceId())).findFirst()
                .orElseThrow(() -> new EntityNotFoundException(ResultState.DataNotFound.getMessage()));
    }

    public ResultData<PayResponseDTO> doHold(PayRequestDTO payDTO) {
        var internalId = UUID.randomUUID();

        var checkRequest = RequestTools.checkRequestBuilder(payDTO, internalId);

        var savedTransaction = prepareNewTransaction(payDTO);

        var savedBill = prepareNewBill(checkRequest, savedTransaction, internalId);

        var checkResponse = getCheckResponse(checkRequest);

        standardUpdateFields(savedTransaction, checkResponse, savedBill);

        if (checkResponse.getStatus() != ResponseStatus.CHECK_OK) {
            saveFailedTransaction(savedTransaction);
            return new ResultData<>(ResultState.FailedResponse, checkResponse.getResponseText());
        }


        var billId = savedTransaction.getPaymentId();

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
            log.warn("Hold rejected - card status: {}, message: {}", cardStatus, errorMessage);
            savedTransaction.setNote(errorMessage);
            saveFailedTransaction(savedTransaction);
            return new ResultData<>(ResultState.FailedResponse, errorMessage);
        }

        var holdActiveRequest = holdActiveRequestBuilder(cardResponse, billId, checkRequest.getAmount());

        savedBill.setPayRequest(holdActiveRequest.toMap());

        var holdActiveResponse = getHoldActiveResponse(holdActiveRequest);


        savedBill.setPayResponse(holdActiveResponse.toMap());
        billService.save(savedBill);

        if (holdActiveResponse.getStatus() != ResponseStatus.HOLD_SUCCESS) {
            handlePayFailedTransaction(savedTransaction, holdActiveResponse, savedBill);
            return new ResultData<>(ResultState.FailedResponse, holdActiveResponse.getResponseText());
        }

        savedTransaction.setStatus(ResponseStatus.HOLD_SUCCESS);
        savedTransaction.setPaymentId(String.valueOf(holdActiveResponse.getId()));
        savedTransaction.setAccount(holdActiveResponse.getAccount());
        //     savedTransaction.setRrn(holdActiveResponse.getSender().getData().getExternalId()); toDo in holdConfirm
        transactionService.save(savedTransaction);

        return new ResultData<>(
                new PayResponseDTO(
                        savedTransaction.getId(),
                        savedTransaction.getPaymentId(),
                        cardResponse.getResult().getMaskedPan(),
                        cardResponse.getResult().getFullName(),
                        cardResponse.getResult().getProcessing(),
                        null,
                        holdActiveResponse.getAmount(),
                        holdActiveResponse.getRecipientShortName(),
                        holdActiveResponse.getAccount(),
                        savedTransaction.getStatus(),
                        savedTransaction.getCreatedTime(),
                        savedTransaction.getUpdatedTime(),
                        savedTransaction.getCreatedBy()
                ));

    }

    private void handlePayFailedTransaction(Transaction savedTransaction,
                                            BillingBaseResponse<?> payResponse, Bill savedBill) {
        savedTransaction.setNote(payResponse.getResponseText());
        savedTransaction.setStatus(payResponse.getStatus());
        transactionService.save(savedTransaction);

        savedBill.setPayResponse(payResponse.toMap());
        billService.save(savedBill);
    }

    private Transaction prepareNewTransaction(PayRequestDTO payDTO) {
        var transaction = new Transaction(
                payDTO.getId(),
                payDTO.getAmount(),
                payDTO.getAccount(),
                ResponseStatus.CREATED
        );

        var tranOptional = transactionService.findTransactionOptionalByInternalId(payDTO.getId());

        if (tranOptional.isPresent())
            throw new EntityNotFoundException("Transaction with internalId: %s has already!".formatted(payDTO.getId()));


        try {
            return transactionService.save(transaction);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException(NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        }
    }


    private Bill prepareNewBill(CheckRequest checkRequest, Transaction savedTransaction, UUID internalId) {
        var bill = new Bill(
                checkRequest,
                savedTransaction.getId(),
                internalId.toString());
        return billService.save(bill);
    }

    private BillingBaseResponse<?> getCheckResponse(CheckRequest checkRequest) {
        try {
            return billingClient.check(checkRequest);
        } catch (Exception e) {
            log.error("Billing check response has error: {}", e.getMessage());
            return new BillingBaseResponse<>(e.getMessage(), ResponseStatus.CHECK_FAIL);
        }
    }

    private void standardUpdateFields(Transaction savedTransaction, BillingBaseResponse<?> checkResponse, Bill savedBill) {
        savedTransaction.setNote(checkResponse.getResponseText());
        savedTransaction.setPaymentId(String.valueOf(checkResponse.getId()));

        savedBill.setCheckResponse(checkResponse.toMap());
        billService.save(savedBill);
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

    private ResultData<CardWithStatusDTO> getCardResponse(PayRequestDTO payDTO) {
        try {
            return cardClientService.getCardById(payDTO.getCard());
        } catch (Exception e) {
            log.error("Card feign client has error: {}", e.getMessage());
            return new ResultData<>(ResultState.FailedResponse, e.getMessage());
        }
    }

    private BillingBaseResponse<Object> getHoldActiveResponse(HoldRequest payRequest) {
        try {
            return billingClient.holdActive(payRequest);
        } catch (Exception e) {
            return new BillingBaseResponse<>(e.getMessage(), ResponseStatus.S_NEED_CONFIRM);
        }
    }

    private BillingBaseResponse<DataResult<SRData>> getHoldConfirmResponse(HoldRequest payRequest) {
        try {
            return billingClient.holdConfirm(payRequest);
        } catch (Exception e) {
            log.info("Debit Confirm failed: {}", e.getMessage());
            return new BillingBaseResponse<>(e.getMessage(), ResponseStatus.S_NEED_CONFIRM);
        }
    }

    private static HoldRequest holdActiveRequestBuilder(ResultData<CardWithStatusDTO> cardResponse,
                                                        String tranId,
                                                        BigDecimal amount) {
        Map<String, String> sentParams = new HashMap<>();
        sentParams.put(CardType.token.toString(), cardResponse.getResult().getToken());

        return new HoldRequest(
                Long.valueOf(tranId),
                amount,
                cardResponse.getResult().getProcessing() == Processing.UZCARD ? PayType.UZCARD : PayType.HUMO,
                sentParams
        );
    }


    @Override
    public ResultData<PayResponseDTO> holdConfirm(PayRequestDTO payDTO) {
        log.info("Starting hold confirm with request: {}", payDTO);

        checkServiceByUser(payDTO.getServiceId());

        var transactionOptional = transactionService.findTransactionOptionalByInternalId(payDTO.getId());

        if (transactionOptional.isEmpty())
            throw new InvalidDataException("Transaction not found");

        var transaction = transactionOptional.get();

        TransChecker.checkUserToTransaction(transaction);

        if (!transaction.getStatus().equals(ResponseStatus.HOLD_SUCCESS))
            throw new InvalidDataException("Transaction already finished");


        if (payDTO.getAmount().compareTo(transaction.getAmount()) > 0) {
            var message = "Pay amount: %s greater than hold amount: %s".formatted(payDTO.getAmount(), transaction.getAmount());
            log.error("Method finished with error: {}", message);
            return new ResultData<>(ResultState.BadRequest, message);
        }

        ResultData<PayResponseDTO> payResponseDTOResultData = doConfirm(payDTO, transaction);

        log.info("Method finished with response dto: {}", payResponseDTOResultData);
        return payResponseDTOResultData;
    }

    @Override
    public ResultData<HoldCancelResponseDTO> cancel(@NotNull Long id) {
        log.info("Received cancel request to tranId: {}", id);

        var transaction = transactionService.findById(id);

        TransChecker.checkUserToTransaction(transaction);

        if (!transaction.getStatus().equals(ResponseStatus.HOLD_SUCCESS))
            throw new GeneralErrorException("You can't cancel hold transaction with status: %s".formatted(transaction.getStatus()));

        log.info("Using paymentId for cancel: {}", transaction.getPaymentId());
        var response = billingClient.holdCancel(Long.valueOf(transaction.getPaymentId()));

        log.info("Billing hold cancel response: {}", response);

        if (!response.getSuccess()) {
            log.error("Billing cancel failed for tranId: {}", id);
            transaction.setStatus(ResponseStatus.CANCEL_FAILED);
            transaction.setNote("Billing hold cancel failed");
            transactionService.save(transaction);
            throw new GeneralErrorException("Отмена холда не удалась");
        }

        transaction.setStatus(ResponseStatus.CANCELLED);
        transactionService.save(transaction);

        return new ResultData<>(
                new HoldCancelResponseDTO(
                        transaction
                )
        );
    }

    @Override
    public ResultData<PayResponseDTO> holdAndConfirm(@NotNull PayRequestDTO payDTO) {
        log.info("Starting hold and confirm with request: {}", payDTO);

        checkServiceByUser(payDTO.getServiceId());

        // Step 1: Do hold
        ResultData<PayResponseDTO> holdResult = doHold(payDTO);

        if (!holdResult.getIsSuccess()) {
            log.warn("Hold failed, cannot proceed with confirm: {}", holdResult.getError());
            return holdResult;
        }

        // Step 2: Get transaction and do confirm
        var transactionOptional = transactionService.findTransactionOptionalByInternalId(payDTO.getId());

        if (transactionOptional.isEmpty()) {
            log.error("Transaction not found after successful hold");
            return new ResultData<>(ResultState.FailedResponse, "Transaction not found after hold");
        }

        var transaction = transactionOptional.get();

        ResultData<PayResponseDTO> confirmResult = doConfirm(payDTO, transaction);

        log.info("Hold and confirm finished with result: {}", confirmResult);
        return confirmResult;
    }

    @Override
    public ResultData<HoldCancelResponseDTO> reverse(@NotNull Long id) {
        log.info("Received reverse request for tranId: {}", id);

        var transaction = transactionService.findById(id);

        TransChecker.checkUserToTransaction(transaction);

        if (!transaction.getStatus().equals(ResponseStatus.SUCCESS)) {
            throw new GeneralErrorException("You can only reverse transaction with status SUCCESS, current status: %s".formatted(transaction.getStatus()));
        }

        log.info("Using paymentId for reverse: {}", transaction.getPaymentId());
        var cancelRequest = new CancelRequest(transaction.getPaymentId());
        var response = billingClient.cancel(cancelRequest);

        log.info("Billing cancel response for reverse: {}", response);

        if (response.getStatus() != ResponseStatus.CANCELLED) {
            log.error("Billing reverse failed for tranId: {}, response: {}", id, response.getResponseText());
            transaction.setStatus(ResponseStatus.CANCEL_FAILED);
            transaction.setNote("Billing reverse failed: " + response.getResponseText());
            transactionService.save(transaction);
            throw new GeneralErrorException("Реверс транзакции не удался");
        }

        transaction.setStatus(ResponseStatus.CANCELLED);
        transaction.setNote("Reversed");
        transactionService.save(transaction);

        return new ResultData<>(
                new HoldCancelResponseDTO(
                        transaction
                )
        );
    }

    private ResultData<PayResponseDTO> doConfirm(PayRequestDTO payDTO, Transaction transaction) {

        var bill = billService.findByTranId(transaction.getId());

        transaction.setAmount(payDTO.getAmount());


        var cardResponse = getCardResponse(payDTO);

        if (!cardResponse.getIsSuccess() && cardResponse.getResult() == null) {
            transaction.setNote(cardResponse.getError().getMessage());
            saveFailedTransaction(transaction);
            return new ResultData<>(ResultState.FailedResponse, cardResponse.getError().getMessage());
        }

        // Check card status - card must be active and not blocked
        var cardStatus = cardResponse.getResult().getStatus();
        if (cardStatus == null || !cardStatus.isActiveAndNotBlocked()) {
            String errorMessage = getCardStatusErrorMessage(cardStatus);
            log.warn("Hold confirm rejected - card status: {}, message: {}", cardStatus, errorMessage);
            transaction.setNote(errorMessage);
            saveFailedTransaction(transaction);
            return new ResultData<>(ResultState.FailedResponse, errorMessage);
        }

        var confirmRequest = holdActiveRequestBuilder(cardResponse, transaction.getPaymentId(), payDTO.getAmount());

        bill.setPayRequest(confirmRequest.toMap());

        var holdActiveResponse = getHoldConfirmResponse(confirmRequest);

        bill.setPayResponse(holdActiveResponse.toMap());


        if (holdActiveResponse.getStatus() != ResponseStatus.SUCCESS) {
            handlePayFailedTransaction(transaction, holdActiveResponse, bill);
            log.info("Method finished with failed status...");
            return new ResultData<>(ResultState.FailedResponse, holdActiveResponse.getResponseText());
        }

        transaction.setStatus(ResponseStatus.SUCCESS);
        transaction.setRrn(holdActiveResponse.getSender().getData().getExternalId());
        transaction.setAccount(holdActiveResponse.getAccount());
        transactionService.save(transaction);

        return new ResultData<>(
                new PayResponseDTO(
                        transaction.getId(),
                        transaction.getPaymentId(),
                        cardResponse.getResult().getMaskedPan(),
                        cardResponse.getResult().getFullName(),
                        cardResponse.getResult().getProcessing(),
                        holdActiveResponse.getSender().getData().getExternalId(),
                        holdActiveResponse.getAmount(),
                        holdActiveResponse.getRecipientShortName(),
                        holdActiveResponse.getAccount(),
                        transaction.getStatus(),
                        transaction.getCreatedTime(),
                        transaction.getUpdatedTime(),
                        transaction.getCreatedBy()
                )
        );
    }
}
