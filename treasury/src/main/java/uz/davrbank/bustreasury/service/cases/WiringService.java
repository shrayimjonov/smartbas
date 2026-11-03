package uz.davrbank.bustreasury.service.cases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.bustreasury.aggregate.dto.request.WiringRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.WiringResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.WiringResultDTO;
import uz.davrbank.bustreasury.aggregate.mapper.WiringMapper;
import uz.davrbank.bustreasury.aggregate.model.*;
import uz.davrbank.bustreasury.exeption.AlreadyExistException;
import uz.davrbank.bustreasury.exeption.DataNotFoundException;
import uz.davrbank.bustreasury.repository.*;
import uz.davrbank.bustreasury.repository.user.SentWiringRepository;
import uz.davrbank.bustreasury.service.interfaces.WiringInterface;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WiringService implements WiringInterface {
    private static final Logger logger = LoggerFactory.getLogger(WiringService.class);

    private final WiringRepository wiringRepository;
    private final PurposeRepository purposeRepository;
    private final MFORepository mfoRepository;
    private final DocTypeRepository docTypeRepository;
    private final TemplateRepository templateRepository;
    private final PaymentRepository paymentRepository;
    private final TypePlatRepository typePlateRepository;
    private final SentWiringRepository sentWiringRepository;

    public WiringService(WiringRepository wiringRepository,
                         PurposeRepository purposeRepository,
                         MFORepository mfoRepository,
                         DocTypeRepository docTypeRepository,
                         TemplateRepository templateRepository,
                         PaymentRepository paymentRepository,
                         TypePlatRepository typePlateRepository,
                         SentWiringRepository sentWiringRepository) {
        this.wiringRepository = wiringRepository;
        this.purposeRepository = purposeRepository;
        this.mfoRepository = mfoRepository;
        this.docTypeRepository = docTypeRepository;
        this.templateRepository = templateRepository;
        this.paymentRepository = paymentRepository;
        this.typePlateRepository = typePlateRepository;
        this.sentWiringRepository = sentWiringRepository;
    }

    /**
     * Создать новую проводку внутри шаблона.
     * Проверяем уникальность по (templateId, wiringId),
     * затем маппим DTO -> entity и сохраняем.
     */
    @Override
    @Transactional
    public WiringResultDTO createWiring(Long wiringId, WiringRequestDTO requestDTO) {
        logger.info("Creating wiring (template={}, wiringId={})", requestDTO.getTemplateId(), wiringId);

        Template template = templateRepository.findById(requestDTO.getTemplateId())
                .orElseThrow(() -> new DataNotFoundException("Template not found: " + requestDTO.getTemplateId()));

        if (wiringRepository.existsByTemplateAndWiringId(template, wiringId)) {
            logger.warn("Wiring already exists in this template (templateId={}, wiringId={})",
                    requestDTO.getTemplateId(), wiringId);
            throw new AlreadyExistException("Проводка с wiringId=" + wiringId + " уже существует для templateId=" + requestDTO.getTemplateId());
        }

        Wiring wiring = WiringMapper.INSTANCE.wiringDTOToWiring(requestDTO);
        wiring.setWiringId(wiringId);
        wiring.setTemplate(template);

        if (requestDTO.getPurposeId() != null) {
            wiring.setPurpose(purposeRepository.findById(requestDTO.getPurposeId()).orElse(null));
        }
        if (requestDTO.getDebitMFOId() != null) {
            wiring.setDebitMFO(mfoRepository.findById(requestDTO.getDebitMFOId()).orElse(null));
        }
        if (requestDTO.getCreditorMFOId() != null) {
            wiring.setCreditMFO(mfoRepository.findById(requestDTO.getCreditorMFOId()).orElse(null));
        }
        if (requestDTO.getDocTypeId() != null) {
            wiring.setDocType(docTypeRepository.findById(requestDTO.getDocTypeId()).orElse(null));
        }
        Payment payment = paymentRepository.findById(3L)
                .orElseThrow(() -> new DataNotFoundException("Payment not found: 3"));
        wiring.setPayment(payment);

        if (requestDTO.getTypePlateId() != null) {
            wiring.setTypePlate(typePlateRepository.findById(requestDTO.getTypePlateId())
                    .orElseThrow(() -> new DataNotFoundException("TypePlate not found")));
        }
        Wiring saved = wiringRepository.save(wiring);
        logger.info("Created wiring id={} (templateId={}, wiringId={})", saved.getId(), template.getId(), wiringId);

        return new WiringResultDTO().successfully(WiringMapper.INSTANCE.wiringToWiringDTO(saved));
    }

    /**
     * Получить все проводки (без фильтра шаблона).
     */
    @Override
    public List<WiringResponseDTO> getAll() {
        logger.info("Retrieving all wirings");
        return wiringRepository.findAll()
                .stream()
                .map(WiringMapper.INSTANCE::wiringToWiringDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получить проводки по templateId.
     */
    @Override
    public List<WiringResponseDTO> getByTemplateId(Long templateId) {
        logger.info("Retrieving wirings by templateId={}", templateId);
        return wiringRepository.findAllByTemplateId(templateId)
                .stream()
                .map(WiringMapper.INSTANCE::wiringToWiringDTO)
                .collect(Collectors.toList());
    }

    /**
     * ⚠️ Был глобальный поиск по wiringId -> заменено на (templateId, wiringId).
     * Если нужен поиск по одному wiringId без template — используйте findAllByWiringId и обрабатывайте множественные результаты.
     */
    public WiringResultDTO getByTemplateIdAndWiringId(Long templateId, Long wiringId) {
        logger.info("Retrieving wiring by (templateId={}, wiringId={})", templateId, wiringId);
        Wiring wiring = wiringRepository.findByTemplateIdAndWiringId(templateId, wiringId)
                .orElseThrow(() -> new DataNotFoundException("Wiring not found by templateId=" + templateId + " and wiringId=" + wiringId));
        return new WiringResultDTO().successfully(WiringMapper.INSTANCE.wiringToWiringDTO(wiring));
    }

    /**
     * Старый метод — если очень нужно, оставьте, но он опасен при дублях.
     * Здесь пример безопасной реализации через List.
     */
    @Override
    public WiringResultDTO getByWiringId(Long wiringId) {
        logger.info("Retrieving wiring by wiringId={} (global, legacy)", wiringId);
        Optional<Wiring> wiring = wiringRepository.findAllByWiringId(wiringId);
        WiringResponseDTO dto = wiring
                .map(WiringMapper.INSTANCE::wiringToWiringDTO)
                .orElseThrow(() -> new DataNotFoundException("Wiring not found by wiringId=" + wiringId));
        return new WiringResultDTO().successfully(dto);
    }

    /**
     * Получить все проводки по Template (entity) c сортировкой по wiringId.
     */
    @Override
    public List<WiringResponseDTO> getAllByTemplateId(Template template) {
        return wiringRepository.findAllByTemplateOrderByWiringId(template)
                .stream()
                .map(WiringMapper.INSTANCE::wiringToWiringDTO)
                .collect(Collectors.toList());
    }

    /**
     * Обновление проводки. Можно сменить связанный Template, DocType и т.д.
     */
    @Override
    public WiringResultDTO update(Long id, WiringRequestDTO requestDTO) {
        logger.info("Updating wiring id={}", id);

        Wiring existing = wiringRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Wiring not found id=" + id));

        WiringMapper.INSTANCE.updateWiringFromDTO(requestDTO, existing);

        if (requestDTO.getPurposeId() != null) {
            existing.setPurpose(purposeRepository.findById(requestDTO.getPurposeId())
                    .orElseThrow(() -> new DataNotFoundException("Purpose not found")));
        }
        if (requestDTO.getDebitMFOId() != null) {
            existing.setDebitMFO(mfoRepository.findById(requestDTO.getDebitMFOId())
                    .orElseThrow(() -> new DataNotFoundException("Debit MFO not found")));
        }
        if (requestDTO.getCreditorMFOId() != null) {
            existing.setCreditMFO(mfoRepository.findById(requestDTO.getCreditorMFOId())
                    .orElseThrow(() -> new DataNotFoundException("Creditor MFO not found")));
        }
        if (requestDTO.getDocTypeId() != null) {
            existing.setDocType(docTypeRepository.findById(requestDTO.getDocTypeId())
                    .orElseThrow(() -> new DataNotFoundException("DocType not found")));
        }
        if (requestDTO.getTemplateId() != null) {
            Template template = templateRepository.findById(requestDTO.getTemplateId())
                    .orElseThrow(() -> new DataNotFoundException("Template not found"));
            existing.setTemplate(template);
        }
        if (requestDTO.getTypePlateId() != null) {
            existing.setTypePlate(typePlateRepository.findById(requestDTO.getTypePlateId())
                    .orElseThrow(() -> new DataNotFoundException("TypePlate not found")));
        }

        Wiring updated = wiringRepository.save(existing);
        return new WiringResultDTO().successfully(WiringMapper.INSTANCE.wiringToWiringDTO(updated));
    }

    /**
     * Удаление проводки + каскадная очистка записей об отправках (sent_wiring).
     */
    @Override
    @Transactional
    public WiringResultDTO delete(Long id) {
        logger.info("Deleting wiring id={}", id);

        if (!wiringRepository.existsById(id)) {
            throw new DataNotFoundException("Wiring not found id=" + id);
        }
        sentWiringRepository.deleteByWiringId(id);
        wiringRepository.deleteById(id);
        return new WiringResultDTO().successfullyWithMessage("Wiring successfully deleted");
    }
}
