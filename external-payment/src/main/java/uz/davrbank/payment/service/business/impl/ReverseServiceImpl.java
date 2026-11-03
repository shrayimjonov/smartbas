package uz.davrbank.payment.service.business.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.model.dto.billing.request.CancelRequest;
import uz.davrbank.payment.model.dto.core.request.CancelRequestDTO;
import uz.davrbank.payment.model.dto.core.response.TransactionResponseDTO;
import uz.davrbank.payment.model.entity.Transaction;
import uz.davrbank.payment.service.business.ReverseService;
import uz.davrbank.payment.service.core.TransactionService;
import uz.davrbank.payment.service.web.BillingClientService;
import uz.davrbank.payment.utils.TransChecker;
import uz.nodir.commondto.exception.InvalidDataException;
import uz.nodir.commondto.exception.ReverseException;
import uz.nodir.commondto.model.dto.core.response.ResultData;
import uz.nodir.commondto.model.enums.ResponseStatus;
import uz.nodir.commondto.utils.SecurityUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReverseServiceImpl implements ReverseService {
    private final TransactionService transactionService;
    private final BillingClientService billingClientService;

    public ResultData<TransactionResponseDTO> cancel(@NotNull CancelRequestDTO requestDTO) {
        var transaction = checkTransaction(requestDTO);


        var cancelRequest = new CancelRequest(transaction.getPaymentId());

        var cancelResponse = billingClientService.cancel(cancelRequest);


        if (cancelResponse.getStatus() != ResponseStatus.CANCELLED) {
            log.error("Billing cancel failed for tranId: {}, response: {}",
                    requestDTO.getTranId(), cancelResponse.getResponseText());
            transaction.setStatus(ResponseStatus.CANCEL_FAILED);
            transaction.setNote("Billing error: " + cancelResponse.getResponseText());
            transactionService.save(transaction);
            throw new ReverseException("Отмена не удалась: " + cancelResponse.getResponseText());
        }

        transaction.setStatus(ResponseStatus.CANCELLED);
        transactionService.save(transaction);

        log.info("Transaction #{} reversed successfully!", requestDTO.getTranId());
        return new ResultData<>(
                new TransactionResponseDTO(transaction)
        );
    }

    private Transaction checkTransaction(CancelRequestDTO requestDTO) {
        var transaction = transactionService.findById(requestDTO.getTranId());

        TransChecker.checkUserToTransaction(transaction);

        if (!transaction.getStatus().equals(ResponseStatus.SUCCESS)
                && !transaction.getStatus().equals(ResponseStatus.CANCEL_FAILED))
            throw new InvalidDataException("Cancel operation for transaction with status: %s not impossible".formatted(transaction.getStatus()));

        return transaction;
    }
}
