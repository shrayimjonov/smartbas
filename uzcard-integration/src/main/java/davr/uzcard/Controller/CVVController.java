package davr.uzcard.Controller;

import davr.uzcard.Model.CardGetCVVRequestDTO;
import davr.uzcard.Service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cvv")
@RequiredArgsConstructor
public class CVVController {
    private final CardService cardService;

    @PostMapping("/get")
    public ResponseEntity<?> getCVV(@RequestBody CardGetCVVRequestDTO requestDTO) {
        return ResponseEntity.ok(cardService.getCVV2(requestDTO));
    }
}
