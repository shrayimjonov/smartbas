package uz.davrbank.bustreasury.endpoint;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.bustreasury.aggregate.dto.request.ExchangeRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.request.TreasuryRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.ResultDTO;
import uz.davrbank.bustreasury.service.interfaces.TreasuryInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/treasury")
@Tag(
        name = "Казначейство",
        description = "Контроллер для операций казначейства — создание казначейских поручений и обменных операций."
)
public class TreasuryController {

    private final TreasuryInterface treasuryInterface;

    public TreasuryController(TreasuryInterface treasuryInterface) {
        this.treasuryInterface = treasuryInterface;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Создание казначейского поручения",
            description = "Запрос должен содержать все обязательные поля DTO."
    )
    public ResultDTO createTreasury(@RequestBody @Valid TreasuryRequestDTO requestDTO) {
        return treasuryInterface.createTreasury(requestDTO);
    }

    @PostMapping("/create/exchange")
    @Operation(
            summary = "Создание обменной операции казначейства",
            description = "Используется для валютных операций между разными счетами."
    )
    public ResultDTO createTreasuryExchange(@RequestBody @Valid ExchangeRequestDTO requestDTO) {
        return treasuryInterface.createTreasuryExchange(requestDTO);
    }
}
