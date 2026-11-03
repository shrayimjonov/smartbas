package uz.davrbank.bustreasury.service.interfaces.user;

import uz.davrbank.bustreasury.aggregate.dto.request.user.CheckStatusRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.user.CheckStatusResponseDTO;

import java.util.List;

public interface CheckStatusInterface {

    List<CheckStatusResponseDTO> getTransactionStatus(CheckStatusRequestDTO request);
}
