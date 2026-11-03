package uz.davrbank.bustreasury.endpoint;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.bustreasury.aggregate.dto.request.PurposeRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.PurposeResponseDTO;
import uz.davrbank.bustreasury.service.interfaces.PurposeInterface;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("api/v1/purpose")
@Tag(
        name = "Назначения платежей",
        description = "Контроллер для управления назначениями платежей — создание, получение, обновление и удаление записей."
)
public class PurposeController {

    private final PurposeInterface purposeInterface;

    public PurposeController(PurposeInterface purposeInterface) {
        this.purposeInterface = purposeInterface;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Создание назначения платежа",
            description = "Добавляет новое назначение платежа в систему."
    )
    public ResponseEntity<?> createPurpose(
            @Valid @RequestBody PurposeRequestDTO requestDTO) {
        return ResponseEntity.ok(purposeInterface.createPurpose(requestDTO));
    }

    @GetMapping("/get/all")
    @Operation(
            summary = "Получение всех назначений платежей",
            description = "Возвращает список всех зарегистрированных назначений платежей."
    )
    public ResponseEntity<List<PurposeResponseDTO>> getAllPurposes() {
        return ResponseEntity.ok(purposeInterface.getAll());
    }

    @GetMapping("/get/{id}")
    @Operation(
            summary = "Получение назначения по ID",
            description = "Возвращает информацию о назначении платежа по указанному идентификатору."
    )
    public ResponseEntity<PurposeResponseDTO> getPurposeById(@PathVariable Long id) {
        return ResponseEntity.ok(purposeInterface.getById(id));
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "Обновление назначения платежа",
            description = "Изменяет данные существующего назначения платежа по ID."
    )
    public ResponseEntity<PurposeResponseDTO> updatePurpose(
            @PathVariable Long id,
            @RequestBody PurposeRequestDTO requestDTO) {
        return ResponseEntity.ok(purposeInterface.update(id, requestDTO));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Удаление назначения платежа",
            description = "Удаляет назначение платежа из системы по указанному идентификатору."
    )
    public ResponseEntity<String> deleteWiring(@PathVariable Long id) {
        return ResponseEntity.ok(purposeInterface.delete(id));
    }
}
