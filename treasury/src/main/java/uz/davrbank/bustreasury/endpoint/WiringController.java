package uz.davrbank.bustreasury.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.bustreasury.aggregate.dto.request.WiringRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.WiringResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.WiringResultDTO;
import uz.davrbank.bustreasury.service.interfaces.WiringInterface;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/wiring")
@Tag(
        name = "Проводки (Wiring)",
        description = "Контроллер для управления проводками — создание, получение, обновление и удаление записей проводок."
)
public class WiringController {

    private final WiringInterface wiringInterface;

    public WiringController(WiringInterface wiringInterface) {
        this.wiringInterface = wiringInterface;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Создание проводки",
            description = "Создаёт новую проводку на основе шаблона и переданных данных. "
                    + "Необходимо указать `wiringId` в параметрах запроса и передать тело в формате `WiringRequestDTO`."
    )
    public WiringResultDTO createTreasury(
            @RequestParam Long wiringId,
            @Valid @RequestBody WiringRequestDTO requestDTO) throws JsonProcessingException {
        return wiringInterface.createWiring(wiringId, requestDTO);
    }

    @GetMapping("/all")
    @Operation(
            summary = "Получение всех проводок",
            description = "Возвращает список всех проводок, зарегистрированных в системе."
    )
    public ResponseEntity<List<WiringResponseDTO>> getAllWirings() {
        return ResponseEntity.ok(wiringInterface.getAll());
    }

    @GetMapping("/all/{templateId}")
    @Operation(
            summary = "Получение проводок по шаблону",
            description = "Возвращает все проводки, относящиеся к определённому шаблону (по `templateId`)."
    )
    public ResponseEntity<List<WiringResponseDTO>> getAllWiringByTemplateId(@PathVariable Long templateId) {
        return ResponseEntity.ok(wiringInterface.getByTemplateId(templateId));
    }

    @GetMapping("/type/{wiringId}")
    @Operation(
            summary = "Получение проводки по ID",
            description = "Возвращает детальную информацию о проводке по её идентификатору (`wiringId`)."
    )
    public ResponseEntity<WiringResultDTO> getWiringById(@PathVariable Long wiringId) {
        return ResponseEntity.ok(wiringInterface.getByWiringId(wiringId));
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "Обновление проводки",
            description = "Обновляет существующую проводку по ID, используя данные из `WiringRequestDTO`."
    )
    public ResponseEntity<WiringResultDTO> updateWiring(
            @PathVariable Long id,
            @RequestBody WiringRequestDTO requestDTO) {
        return ResponseEntity.ok(wiringInterface.update(id, requestDTO));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Удаление проводки",
            description = "Удаляет проводку по указанному идентификатору (`id`)."
    )
    public ResponseEntity<WiringResultDTO> deleteWiring(@PathVariable Long id) {
        return ResponseEntity.ok(wiringInterface.delete(id));
    }
}
