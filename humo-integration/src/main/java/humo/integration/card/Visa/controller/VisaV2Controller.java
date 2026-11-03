package humo.integration.card.Visa.controller;

import humo.integration.card.Visa.Model.InfoDTO;
import humo.integration.card.Visa.Service.VisaV2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class VisaV2Controller {
    private final VisaV2Service visaV2Service;

    @PostMapping("/info")
    public ResponseEntity<?> getCardInfo(@RequestBody @Valid InfoDTO dto) {
        return ResponseEntity.ok(visaV2Service.cardInfo(dto));
    }

    @PostMapping("/balance")
    public ResponseEntity<?> getCardLittleInfo(@RequestBody @Valid InfoDTO dto) {
        return ResponseEntity.ok(visaV2Service.getCardBalance(dto));
    }

    @GetMapping("/realPan")
    public ResponseEntity<?> getRealPan(@RequestParam("token") String token) {
        return ResponseEntity.ok(visaV2Service.takeRealPan(token));
    }
}
