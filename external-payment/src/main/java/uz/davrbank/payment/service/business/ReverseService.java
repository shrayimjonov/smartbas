package uz.davrbank.payment.service.business;

import jakarta.validation.constraints.NotNull;
import uz.davrbank.payment.model.dto.core.request.CancelRequestDTO;
import uz.davrbank.payment.model.dto.core.response.TransactionResponseDTO;
import uz.nodir.commondto.model.dto.core.response.ResultData;

public interface ReverseService {
    ResultData<TransactionResponseDTO> cancel(@NotNull CancelRequestDTO requestDTO);
}
