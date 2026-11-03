package davr.uzcard.Controller;

import davr.uzcard.Model.LimitAddDto;
import davr.uzcard.Model.LimitInfoDto;
import davr.uzcard.Model.LimitRemoveDto;
import davr.uzcard.Service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/limit")
@RequiredArgsConstructor
public class LimitController {
    private final CardService cardService;

    @GetMapping
    public ResponseEntity<?> getLimits() {
        return ResponseEntity.ok(cardService.getCardLimitList());
    }

    @PostMapping("/card")
    public ResponseEntity<?> getCardLimits(@RequestBody @Valid LimitInfoDto dto) {
        return ResponseEntity.ok(cardService.getCardLimit(dto));
    }

    @PostMapping
    public ResponseEntity<?> addLimit(@RequestBody @Valid LimitAddDto dto) {
        return ResponseEntity.ok(cardService.addLimitToCard(dto));
    }

    @DeleteMapping
    public ResponseEntity<?> removeLimit(@RequestBody @Valid LimitRemoveDto dto) {
        return ResponseEntity.ok(cardService.removeLimitFromCard(dto));
    }


}
