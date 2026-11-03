package uz.davrbank.bustreasury.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import uz.davrbank.bustreasury.aggregate.dto.request.WiringRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.WiringResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.WiringResultDTO;
import uz.davrbank.bustreasury.aggregate.model.Template;

import java.util.List;


public interface WiringInterface {
    WiringResultDTO createWiring( Long wiringId, WiringRequestDTO requestDTO) throws JsonProcessingException;

    List<WiringResponseDTO> getAll();

    List<WiringResponseDTO> getByTemplateId(Long id);

    WiringResultDTO getByWiringId(Long wiringId);

    WiringResultDTO update(Long id, WiringRequestDTO requestDTO);

    WiringResultDTO delete(Long id);

    List<WiringResponseDTO> getAllByTemplateId(Template template);
}
