package uz.davrbank.payment.service.business;

import jakarta.validation.constraints.NotNull;
import uz.davrbank.payment.model.dto.core.request.PayRequestDTO;
import uz.davrbank.payment.model.dto.core.response.HoldCancelResponseDTO;
import uz.davrbank.payment.model.dto.core.response.PayResponseDTO;
import uz.nodir.commondto.model.dto.core.response.ResultData;

public interface PayHoldService {
    ResultData<PayResponseDTO> holdActive(@NotNull PayRequestDTO payDTO);

    ResultData<PayResponseDTO> holdConfirm(@NotNull PayRequestDTO payDTO);

    ResultData<PayResponseDTO> holdAndConfirm(@NotNull PayRequestDTO payDTO);

    ResultData<HoldCancelResponseDTO> cancel(@NotNull Long id);

    ResultData<HoldCancelResponseDTO> reverse(@NotNull Long id);
}
