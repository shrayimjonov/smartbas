package uz.davrbank.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.payment.model.dto.core.request.CancelRequestDTO;
import uz.davrbank.payment.model.dto.core.request.PayRequestDTO;
import uz.davrbank.payment.service.business.DebitCardService;
import uz.davrbank.payment.service.business.ReverseService;

@RestController
@RequestMapping("/api/v1/pay")
@RequiredArgsConstructor
public class PayController {
    private final DebitCardService debitCardService;
    private final ReverseService reverseService;

    @PostMapping
    public ResponseEntity<?> pay(@RequestBody @Valid PayRequestDTO requestDTO) {
        return ResponseEntity.ok(debitCardService.pay(requestDTO));
    }

    @PatchMapping("/cancel")
    public ResponseEntity<?> cancel(@RequestBody @Valid CancelRequestDTO requestDTO) {
        return ResponseEntity.ok(reverseService.cancel(requestDTO));
    }
}
