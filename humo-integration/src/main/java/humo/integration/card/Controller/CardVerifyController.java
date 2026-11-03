package humo.integration.card.Controller;

import humo.integration.card.Model.Dto.core.request.CardConfirmRequestDTO;
import humo.integration.card.Model.Dto.core.request.CardVerifyRequestDTO;
import humo.integration.card.Service.v2.CardVerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/add")
@RequiredArgsConstructor
public class CardVerifyController {
    private final CardVerifyService cardVerifyService;


    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody @Valid CardVerifyRequestDTO requestDTO) {
        return ResponseEntity.ok(cardVerifyService.verify(requestDTO));
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestBody @Valid CardConfirmRequestDTO requestDTO) {
        return ResponseEntity.ok(cardVerifyService.confirm(requestDTO));
    }
}
