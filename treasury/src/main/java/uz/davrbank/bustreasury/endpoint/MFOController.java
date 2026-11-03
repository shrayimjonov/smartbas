package uz.davrbank.bustreasury.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.bustreasury.aggregate.dto.request.MFORequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.MFOResponseDTO;
import uz.davrbank.bustreasury.service.interfaces.MFOInterface;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/mfo")
@Tag(
        name = "МФО (Банковские филиалы)",
        description = "Контроллер для управления МФО — создание, получение по ID и список всех филиалов банка."
)
public class MFOController {

    private final MFOInterface mfoInterface;

    public MFOController(MFOInterface mfoInterface) {
        this.mfoInterface = mfoInterface;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Создание нового МФО",
            description = "Добавляет новый банковский филиал (МФО) в систему."
    )
    public ResponseEntity<MFOResponseDTO> createMFO(@RequestBody MFORequestDTO requestDTO) {
        return ResponseEntity.ok(mfoInterface.createMFO(requestDTO));
    }

    @GetMapping("/get/{id}")
    @Operation(
            summary = "Получение МФО по ID",
            description = "Возвращает информацию о конкретном МФО по его уникальному идентификатору."
    )
    public ResponseEntity<MFOResponseDTO> getMFOById(@PathVariable Long id) {
        return ResponseEntity.ok(mfoInterface.getMFOById(id));
    }

    @GetMapping("/get/all")
    @Operation(
            summary = "Получение всех МФО",
            description = "Возвращает список всех зарегистрированных филиалов (МФО)."
    )
    public ResponseEntity<List<MFOResponseDTO>> getAllMFOs() {
        return ResponseEntity.ok(mfoInterface.getAllMFOs());
    }
}
