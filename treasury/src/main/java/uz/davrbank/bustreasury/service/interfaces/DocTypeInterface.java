package uz.davrbank.bustreasury.service.interfaces;

import uz.davrbank.bustreasury.aggregate.dto.request.DocTypeRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.DocTypeResponseDTO;

import java.util.List;


public interface DocTypeInterface {
    DocTypeResponseDTO createDocType(DocTypeRequestDTO requestDTO);

    DocTypeResponseDTO getDocTypeById(Long id);

    List<DocTypeResponseDTO> getAllDocTypes();

    DocTypeResponseDTO updateDocType(Long id, DocTypeRequestDTO requestDTO);

    void deleteDocType(Long id);
}
