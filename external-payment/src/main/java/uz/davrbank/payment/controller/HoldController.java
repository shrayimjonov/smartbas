package uz.davrbank.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.payment.model.dto.core.request.PayRequestDTO;
import uz.davrbank.payment.service.business.PayHoldService;

@RestController
@RequestMapping("/api/v1/hold")
@RequiredArgsConstructor
public class HoldController {
    private final PayHoldService payHoldService;

    @PostMapping("/active")
    public ResponseEntity<?> pay(@RequestBody @Valid PayRequestDTO requestDTO) {
        return ResponseEntity.ok(payHoldService.holdActive(requestDTO));
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestBody @Valid PayRequestDTO requestDTO) {
        return ResponseEntity.ok(payHoldService.holdConfirm(requestDTO));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancel(@PathVariable("id") Long id) {
        return ResponseEntity.ok(payHoldService.cancel(id));
    }

    @PostMapping("/pay")
    public ResponseEntity<?> holdAndConfirm(@RequestBody @Valid PayRequestDTO requestDTO) {
        return ResponseEntity.ok(payHoldService.holdAndConfirm(requestDTO));
    }

    @PostMapping("/reverse/{id}")
    public ResponseEntity<?> reverse(@PathVariable("id") Long id) {
        return ResponseEntity.ok(payHoldService.reverse(id));
    }

}
