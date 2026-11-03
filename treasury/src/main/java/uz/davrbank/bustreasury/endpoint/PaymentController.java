package uz.davrbank.bustreasury.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.bustreasury.aggregate.dto.request.PaymentRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.PaymentResponseDTO;
import uz.davrbank.bustreasury.service.interfaces.PaymentInterface;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/payment")
@Tag(
        name = "Платежи",
        description = "Контроллер для управления платежами — создание, получение, удаление и просмотр списка всех платежей."
)
public class PaymentController {

    private final PaymentInterface paymentInterface;

    public PaymentController(PaymentInterface paymentInterface) {
        this.paymentInterface = paymentInterface;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Создание платежа",
            description = "Добавляет новый платёж в систему."
    )
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO requestDTO) {
        return ResponseEntity.ok(paymentInterface.createPayment(requestDTO));
    }

    @GetMapping("/get/{id}")
    @Operation(
            summary = "Получение платежа по ID",
            description = "Возвращает информацию о платеже по его уникальному идентификатору."
    )
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentInterface.getPaymentById(id));
    }

    @GetMapping("/get/all")
    @Operation(
            summary = "Получение всех платежей",
            description = "Возвращает список всех зарегистрированных платежей."
    )
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentInterface.getAllPayments());
    }

    @DeleteMapping("/delete/by/id")
    @Operation(
            summary = "Удаление платежа по ID",
            description = "Удаляет платёж из системы по указанному идентификатору и возвращает сообщение о результате операции."
    )
    public ResponseEntity<String> deleteById(@RequestParam Long id) {
        String message = paymentInterface.deleteById(id);
        return ResponseEntity.ok(message);
    }
}
