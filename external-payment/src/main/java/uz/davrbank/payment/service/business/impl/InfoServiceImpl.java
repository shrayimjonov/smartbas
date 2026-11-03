package uz.davrbank.payment.service.business.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.model.dto.core.request.TranCheckRequestDTO;
import uz.davrbank.payment.model.dto.core.response.TransactionResponseDTO;
import uz.davrbank.payment.service.business.InfoService;
import uz.davrbank.payment.service.core.TransactionService;
import uz.davrbank.payment.utils.TransChecker;
import uz.nodir.commondto.exception.EntityNotFoundException;
import uz.nodir.commondto.model.dto.core.response.ResultData;

@Service
@Slf4j
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {
    private final TransactionService transactionService;

    @Override
    public ResultData<TransactionResponseDTO> findTransactionById(@NotNull Long id) {
        var transaction = transactionService.findById(id);

        TransChecker.checkUserToTransaction(transaction);

        return new ResultData<>(
                new TransactionResponseDTO(transaction)
        );
    }


    @Override
    public ResultData<TransactionResponseDTO> findTransactionByInternalId(@NotNull TranCheckRequestDTO requestDTO) {
        log.info("Check tran by external id: {}", requestDTO.getExtId());

        var transactionOptional = transactionService.findTransactionOptionalByInternalId(requestDTO.getExtId());

        if (transactionOptional.isEmpty())
            throw new EntityNotFoundException("Transaction not found");

        var transaction = transactionOptional.get();

        TransChecker.checkUserToTransaction(transaction);

        return new ResultData<>(
                new TransactionResponseDTO(transaction)
        );
    }
}
