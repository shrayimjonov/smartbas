package davr.uzcard.Controller;

import davr.uzcard.Model.CardsNewDto;
import davr.uzcard.Model.CardsNewVerifyDto;
import davr.uzcard.Model.CheckPinflDTO;
import davr.uzcard.Model.TokenListDTO;
import davr.uzcard.Service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class InfoController {
    private final CardService cardService;

    @PostMapping("/otp")
    public ResponseEntity<?> cardInfoOtp(@RequestBody CardsNewDto dto) {
        return ResponseEntity.ok(cardService.cardsNewOTP(dto));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> cardInfoVerify(@RequestBody CardsNewVerifyDto dto) {
        return ResponseEntity.ok(cardService.cardsNewVerify(dto));
    }

    @PostMapping("/checkCardToPinfl")
    public ResponseEntity<?> checkCardToPinfl(@RequestBody CheckPinflDTO dto) {
        return ResponseEntity.ok(cardService.checkPinfl(dto));
    }

    @PostMapping("/tokenList")
    public ResponseEntity<?> getConnectedApps(@RequestBody TokenListDTO dto) {
        return ResponseEntity.ok(cardService.getCardConnectedApps(dto));
    }
}
