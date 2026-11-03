package davr.uzcard.Controller;

import davr.uzcard.Model.PinChangeDTO;
import davr.uzcard.Model.PinSetDTO;
import davr.uzcard.Service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/pin")
@RequiredArgsConstructor
public class PinController {
    private final CardService cardService;

    @PostMapping("/change")
    public ResponseEntity<?> pinChange(@RequestBody @Valid PinChangeDTO pinChangeDTO) {
        return ResponseEntity.ok(cardService.pinChange(pinChangeDTO));
    }

    @PostMapping("/set")
    public ResponseEntity<?> pinSet(@RequestBody @Valid PinSetDTO pinSetDTO) {
        return ResponseEntity.ok(cardService.pinSet(pinSetDTO));
    }
}
