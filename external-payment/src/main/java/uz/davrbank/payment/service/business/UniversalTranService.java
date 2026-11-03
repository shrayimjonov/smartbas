package uz.davrbank.payment.service.business;

import uz.davrbank.payment.model.dto.tran.request.TranBaseRequestDTO;
import uz.davrbank.payment.model.dto.tran.response.TranBaseResponseDTO;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;
import uz.nodir.commondto.model.enums.ResultState;


public sealed interface UniversalTranService permits UzCardTranService, HuMoTranService, NCITranService {

    default SimpleResponse<TranBaseResponseDTO> credit(TranBaseRequestDTO requestDTO) {
        return new SimpleResponse<>(ResultState.IncorrectProcessing.getCode(), "NOT IMPLEMENTED");
    }

    default SimpleResponse<TranBaseResponseDTO> debit(TranBaseRequestDTO requestDTO) {
        return new SimpleResponse<>(ResultState.IncorrectProcessing.getCode(), "NOT IMPLEMENTED");
    }


}
