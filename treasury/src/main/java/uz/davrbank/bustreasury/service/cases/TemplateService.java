package uz.davrbank.bustreasury.service.cases;

import org.springframework.stereotype.Service;
import uz.davrbank.bustreasury.aggregate.dto.request.TemplateRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.TemplateResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.WiringResponseDTO;
import uz.davrbank.bustreasury.util.CurrencyType;
import uz.davrbank.bustreasury.aggregate.model.Template;
import uz.davrbank.bustreasury.exeption.DataNotFoundException;
import uz.davrbank.bustreasury.repository.TemplateRepository;
import uz.davrbank.bustreasury.service.interfaces.TemplateInterface;
import uz.davrbank.bustreasury.service.interfaces.WiringInterface;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateService implements TemplateInterface {
    private final TemplateRepository templateRepository;
    private final WiringInterface wiringInterface;

    public TemplateService(TemplateRepository templateRepository, WiringInterface wiringInterface) {
        this.templateRepository = templateRepository;
        this.wiringInterface = wiringInterface;
    }

    @Override
    public TemplateResponseDTO createTemplate(TemplateRequestDTO requestDTO, CurrencyType currencyType) {
        Template template = new Template();
        template.setName(requestDTO.getName());
        template.setDescription(requestDTO.getDescription());
        Template save = templateRepository.save(template);
        TemplateResponseDTO responseDTO = new TemplateResponseDTO();
        responseDTO.setName(save.getName());
        responseDTO.setDescription(save.getDescription());
        responseDTO.setCurrency(save.getCurrency());
        responseDTO.setId(save.getId());
//        List<WiringResponseDTO> allByTemplateId = wiringInterface.getAllByTemplateId(save);
//        responseDTO.setWiring(allByTemplateId);
        return responseDTO;
    }

    @Override
    public TemplateResponseDTO getTemplateById(Long id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Template  not found for id " + id));

        TemplateResponseDTO responseDTO = new TemplateResponseDTO();
        responseDTO.setName(template.getName());
        responseDTO.setDescription(template.getDescription());
        responseDTO.setId(template.getId());
        List<WiringResponseDTO> allByTemplateId = wiringInterface.getAllByTemplateId(template);
        responseDTO.setWiring(allByTemplateId);
        return responseDTO;
    }


    @Override
    public List<TemplateResponseDTO> getAllTemplates() {
        List<Template> all = templateRepository.findAll();
        List<TemplateResponseDTO> responseDTOs = new ArrayList<>();
        for (Template template : all) {
            TemplateResponseDTO responseDTO = new TemplateResponseDTO();
            responseDTO.setName(template.getName());
            responseDTO.setDescription(template.getDescription());
            responseDTO.setId(template.getId());
            responseDTO.setCurrency(template.getCurrency());
            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }

    @Override
    public TemplateResponseDTO updateTemplate(Long id, TemplateRequestDTO requestDTO) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Template not found for id " + id));
        template.setName(requestDTO.getName() != null ? requestDTO.getName() : template.getName());
        template.setDescription(requestDTO.getDescription() != null ? requestDTO.getDescription() : template.getDescription());
        Template updatedTemplate = templateRepository.save(template);
        TemplateResponseDTO responseDTO = new TemplateResponseDTO();
        responseDTO.setName(updatedTemplate.getName());
        responseDTO.setDescription(updatedTemplate.getDescription());
        responseDTO.setId(updatedTemplate.getId());
//        List<WiringResponseDTO> allByTemplateId = wiringInterface.getAllByTemplateId(updatedTemplate);
//        responseDTO.setWiring(allByTemplateId);
        return responseDTO;
    }

    @Override
    public void deleteTemplate(Long id) {
        templateRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Template not found for id " + id));
        templateRepository.deleteById(id);
        // Optionally, you can also delete associated wirings if needed
        // List<WiringResponseDTO> wirings = wiringInterface.getAllByTemplateId(templateRepository.findById(id)
//                .orElseThrow(() -> new DataNotFoundException("Template not found for id " + id)));
        // for (WiringResponseDTO wiring : wirings) {
        //     wiringInterface.delete(wiring.getId());
        // }
    }



}
