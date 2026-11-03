package humo.integration.card.Controller;

import humo.integration.card.Model.Dto.HumoHistoryRequestDTO;
import humo.integration.card.Service.FeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {
    private final FeignService feignService;

    @PostMapping
    public ResponseEntity<?> getCardHistory(@RequestBody @Valid HumoHistoryRequestDTO requestDTO) {
        return ResponseEntity.ok(
                feignService.getCardHistory(requestDTO)
        );
    }
}
