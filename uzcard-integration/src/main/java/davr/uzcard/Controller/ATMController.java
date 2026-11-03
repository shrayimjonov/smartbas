package davr.uzcard.Controller;

import davr.uzcard.Model.CashOutDTO;
import davr.uzcard.Service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/atm")
@RequiredArgsConstructor
public class ATMController {
    private final CardService cardService;

    @PostMapping("/cashOut")
    public ResponseEntity<?> getCashOutCode(@RequestBody @Valid CashOutDTO dto) {
        return ResponseEntity.ok(
                cardService.cashOut(dto)
        );
    }
}
