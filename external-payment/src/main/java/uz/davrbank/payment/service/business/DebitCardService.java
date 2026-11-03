package uz.davrbank.payment.service.business;

import uz.davrbank.payment.model.dto.core.request.PayRequestDTO;
import uz.davrbank.payment.model.dto.core.response.PayResponseDTO;
import uz.nodir.commondto.model.dto.core.response.ResultData;

public interface DebitCardService {

    ResultData<PayResponseDTO> pay(PayRequestDTO payDTO);
}
