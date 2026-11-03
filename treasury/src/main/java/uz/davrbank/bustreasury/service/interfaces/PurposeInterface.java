package uz.davrbank.bustreasury.service.interfaces;

import jakarta.validation.Valid;
import uz.davrbank.bustreasury.aggregate.dto.request.PurposeRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.PurposeResponseDTO;

import java.util.List;


public interface PurposeInterface {
    PurposeResponseDTO createPurpose(@Valid PurposeRequestDTO requestDTO);

    List<PurposeResponseDTO> getAll();

    PurposeResponseDTO update(Long id, PurposeRequestDTO requestDTO);

    String delete(Long id);

    PurposeResponseDTO getById(Long id);

}