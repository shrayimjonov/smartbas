package uz.davrbank.bustreasury.service.interfaces.user;

import uz.davrbank.bustreasury.aggregate.dto.request.user.OperationUserRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.user.OperationUserResponseDTO;

public interface WiringInterfaceUSER {

    OperationUserResponseDTO createWiringOperations(OperationUserRequestDTO operationUserRequestDTO);
}
