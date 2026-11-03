package humo.integration.card.Controller;

import humo.integration.card.Service.RestrictCardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restrict")
@RequiredArgsConstructor
public class RestrictCardsController {
    private final RestrictCardsService restrictCardsService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(restrictCardsService.getBlockedCards());
    }

    @PostMapping("/byCard")
    public ResponseEntity<?> getByCard(@RequestBody Object request) {
        return ResponseEntity.ok(restrictCardsService.getCard(request));
    }

    @PostMapping("/addCard")
    public ResponseEntity<?> addCard(@RequestBody Object request) {
        return ResponseEntity.ok(restrictCardsService.addCard(request));
    }

    @PostMapping("/removeCard")
    public ResponseEntity<?> removeCard(@RequestBody Object request) {
        return ResponseEntity.ok(restrictCardsService.removeCard(request));
    }
}
