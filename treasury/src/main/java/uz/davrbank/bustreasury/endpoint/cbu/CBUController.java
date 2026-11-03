package uz.davrbank.bustreasury.endpoint.cbu;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.bustreasury.aggregate.dto.response.cbu.CurrencyRateResponseDTO;
import uz.davrbank.bustreasury.service.interfaces.CBUCallerInterface;

@RestController
@RequestMapping("/api/v1/cbu/currency/daily")
public class CBUController {

    private final CBUCallerInterface cbuCallerInterface;

    public CBUController(CBUCallerInterface cbuCallerInterface) {
        this.cbuCallerInterface = cbuCallerInterface;
    }

    @PostMapping("/info")
    public ResponseEntity<CurrencyRateResponseDTO[]> callToCBU() {
        return ResponseEntity.ok(cbuCallerInterface.getLatestRateUpdate());
    }
}
