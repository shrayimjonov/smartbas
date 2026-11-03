package humo.integration.card.Controller;

import humo.integration.card.Service.FeignServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scoring")
@RequiredArgsConstructor
public class ScoringController {
    private final FeignServiceImpl feignService;

    @PostMapping("/byCard")
    public ResponseEntity<?> sortedScoringByCard(@RequestBody Object request) {
        return ResponseEntity.ok(feignService.tranSortedScoring(request));
    }

    @PostMapping("/byPinfl")
    public ResponseEntity<?> scoringByPINFL(@RequestBody Object request) {
        return ResponseEntity.ok(feignService.scoringByPersonCode(request));
    }

}
