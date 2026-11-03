package uz.davrbank.bustreasury.endpoint;

import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.bustreasury.aggregate.dto.request.TemplateRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.TemplateResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.Template;
import uz.davrbank.bustreasury.repository.TemplateRepository;
import uz.davrbank.bustreasury.service.interfaces.TemplateInterface;
import uz.davrbank.bustreasury.util.CurrencyType;
import uz.davrbank.bustreasury.util.OperationType;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/template")
@Tag(
        name = "Шаблоны проводок",
        description = "Контроллер для управления шаблонами проводок: создание, получение, обновление, удаление и служебные выборки."
)
public class TemplateController {

    private final TemplateInterface templateInterface;
    private final TemplateRepository templateRepository;
    private final uz.davrbank.bustreasury.service.cases.user.WiringOperations wiringOperations;

    public TemplateController(TemplateInterface templateInterface,
                              TemplateRepository templateRepository,
                              uz.davrbank.bustreasury.service.cases.user.WiringOperations wiringOperations) {
        this.templateInterface = templateInterface;
        this.templateRepository = templateRepository;
        this.wiringOperations = wiringOperations;
    }

    @PermitAll
    @PostMapping("/create")
    @Operation(
            summary = "Создать шаблон",
            description = "Создаёт новый шаблон проводок. Валюта передаётся как query-параметр `currencyType`."
    )
    public ResponseEntity<TemplateResponseDTO> createTemplate(
            @RequestBody TemplateRequestDTO requestDTO,
            @RequestParam CurrencyType currencyType
    ) {
        return ResponseEntity.ok(templateInterface.createTemplate(requestDTO, currencyType));
    }

    @PermitAll
    @GetMapping("/get/{id}")
    @Operation(
            summary = "Получить шаблон по ID",
            description = "Возвращает данные шаблона по указанному идентификатору."
    )
    public ResponseEntity<TemplateResponseDTO> getTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(templateInterface.getTemplateById(id));
    }

    @PermitAll
    @GetMapping("/get/all")
    @Operation(
            summary = "Список всех шаблонов (DTO)",
            description = "Возвращает список всех шаблонов в виде DTO для клиентского интерфейса."
    )
    public ResponseEntity<List<TemplateResponseDTO>> getAllTemplates() {
        return ResponseEntity.ok(templateInterface.getAllTemplates());
    }

    @PermitAll
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Обновить шаблон",
            description = "Изменяет данные существующего шаблона по ID."
    )
    public ResponseEntity<TemplateResponseDTO> updateTemplate(
            @PathVariable Long id,
            @RequestBody TemplateRequestDTO requestDTO) {
        return ResponseEntity.ok(templateInterface.updateTemplate(id, requestDTO));
    }

    @PermitAll
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить шаблон",
            description = "Удаляет шаблон по указанному идентификатору."
    )
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateInterface.deleteTemplate(id);
        return ResponseEntity.ok().build();
    }

    @PermitAll
    @GetMapping("/all")
    @Operation(
            summary = "Сырые записи шаблонов (ENTITY)",
            description = "Возвращает все шаблоны напрямую из БД (ENTITY). Полезно для административных задач и отладки."
    )
    public ResponseEntity<List<Template>> getAllTemplatesTest() {
        List<Template> templates = templateRepository.findAll();
        return ResponseEntity.ok(templates);
    }

    @PermitAll
    @GetMapping("/filter")
    @Operation(
            summary = "Найти шаблон по валюте и операции",
            description = "Ищет шаблон по параметрам `currency` (USD/EUR/RUB...) и `operation` (BUY/SELL)."
    )
    public ResponseEntity<Template> getByCurrencyAndOperation(
            @RequestParam String currency,
            @RequestParam String operation) {
        return templateRepository.findByCurrencyAndOperation(
                        Enum.valueOf(CurrencyType.class, currency.toUpperCase()),
                        Enum.valueOf(OperationType.class, operation.toUpperCase())
                )
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
