package uz.davrbank.bustreasury.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.bustreasury.aggregate.dto.request.TypePlatRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.TypePlatResponseDTO;
import uz.davrbank.bustreasury.service.interfaces.TypePlatInterface;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/type/plate")
@Tag(
        name = "Типы платежей (Type Plate)",
        description = "Контроллер для управления типами платежей — создание, получение по ID и список всех типов."
)
public class TypePlatController {

    private final TypePlatInterface typeplateInterface;

    public TypePlatController(TypePlatInterface typeplateInterface) {
        this.typeplateInterface = typeplateInterface;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Создание типа платежа",
            description = "Создаёт новый тип платежа в системе. Необходимо передать корректный объект запроса TypePlatRequestDTO."
    )
    public ResponseEntity<TypePlatResponseDTO> createPayment(@RequestBody TypePlatRequestDTO requestDTO) {
        return ResponseEntity.ok(typeplateInterface.createPayment(requestDTO));
    }

    @GetMapping("/get/{id}")
    @Operation(
            summary = "Получение типа платежа по ID",
            description = "Возвращает информацию о типе платежа по указанному идентификатору."
    )
    public ResponseEntity<TypePlatResponseDTO> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(typeplateInterface.getPaymentById(id));
    }

    @GetMapping("/get/all")
    @Operation(
            summary = "Получение всех типов платежей",
            description = "Возвращает список всех типов платежей, зарегистрированных в системе."
    )
    public ResponseEntity<List<TypePlatResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(typeplateInterface.getAllPayments());
    }
}
