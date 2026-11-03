package uz.davrbank.bustreasury.endpoint.user;

import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.bustreasury.aggregate.dto.request.user.CheckStatusRequestDTO;
import uz.davrbank.bustreasury.service.interfaces.user.CheckStatusInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("api/v1/user/check")
@Tag(
        name = "Проверка статуса транзакций",
        description = "Контроллер предназначен для проверки статусов пользовательских транзакций через backend-сервис."
)
public class CheckStatusController {

    private final CheckStatusInterface checkStatusInterface;

    public CheckStatusController(CheckStatusInterface checkStatusInterface) {
        this.checkStatusInterface = checkStatusInterface;
    }

    @PermitAll
    @PostMapping("/transaction/status")
    @Operation(
            summary = "Проверка статуса транзакции",
            description = "Возвращает актуальный статус транзакции по данным, переданным в теле запроса."
    )
    public ResponseEntity<?> createDocType(@RequestBody CheckStatusRequestDTO requestDTO) {
        return ResponseEntity.ok(checkStatusInterface.getTransactionStatus(requestDTO));
    }
}
