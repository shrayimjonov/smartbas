package davr.uzcard.Controller;

import davr.uzcard.Service.FeignImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GenesisController {
    private final FeignImpl feign;

    @PostMapping("/genesis")
    public ResponseEntity<?> genesisConnect(@RequestBody Object dto) {
        return ResponseEntity.ok(feign.genesis(dto));
    }
}
