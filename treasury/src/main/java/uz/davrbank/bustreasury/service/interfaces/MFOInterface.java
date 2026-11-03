package uz.davrbank.bustreasury.service.interfaces;

import uz.davrbank.bustreasury.aggregate.dto.request.MFORequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.MFOResponseDTO;

import java.util.List;


public interface MFOInterface {
    MFOResponseDTO createMFO(MFORequestDTO requestDTO);

    MFOResponseDTO getMFOById(Long id);

    List<MFOResponseDTO> getAllMFOs();
}
