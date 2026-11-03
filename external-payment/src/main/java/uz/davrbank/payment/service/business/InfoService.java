package uz.davrbank.payment.service.business;

import jakarta.validation.constraints.NotNull;
import uz.davrbank.payment.model.dto.core.request.TranCheckRequestDTO;
import uz.davrbank.payment.model.dto.core.response.TransactionResponseDTO;
import uz.nodir.commondto.model.dto.core.response.ResultData;

public interface InfoService {
    ResultData<TransactionResponseDTO> findTransactionById(@NotNull Long id);

    ResultData<TransactionResponseDTO> findTransactionByInternalId(@NotNull TranCheckRequestDTO requestDTO);
}
