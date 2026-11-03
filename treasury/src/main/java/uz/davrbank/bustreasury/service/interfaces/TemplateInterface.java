package uz.davrbank.bustreasury.service.interfaces;

import uz.davrbank.bustreasury.aggregate.dto.request.TemplateRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.TemplateResponseDTO;
import uz.davrbank.bustreasury.util.CurrencyType;

import java.util.List;


public interface TemplateInterface {

    TemplateResponseDTO createTemplate(TemplateRequestDTO requestDTO, CurrencyType currencyType);

    TemplateResponseDTO getTemplateById(Long id);

    List<TemplateResponseDTO> getAllTemplates();

    TemplateResponseDTO updateTemplate(Long id, TemplateRequestDTO requestDTO);

    void deleteTemplate(Long id);
}
