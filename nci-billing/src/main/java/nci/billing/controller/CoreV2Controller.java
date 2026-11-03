package nci.billing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nci.billing.model.dto.PayDtoV2;
import nci.billing.service.CoreV2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/core")
@RequiredArgsConstructor
public class CoreV2Controller {
    private final CoreV2Service coreV2Service;

    @PostMapping("/pay")
    public ResponseEntity<?> payV2(@RequestBody @Valid PayDtoV2 dto) {
        return ResponseEntity.ok(coreV2Service.pay(dto));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkV2(@RequestParam("paymentId") String paymentId) {
        return ResponseEntity.ok(coreV2Service.check(paymentId));
    }
}
