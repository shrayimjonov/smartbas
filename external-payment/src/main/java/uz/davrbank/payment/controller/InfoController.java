package uz.davrbank.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.payment.model.dto.core.request.TranCheckRequestDTO;
import uz.davrbank.payment.service.business.InfoService;

@RestController
@RequestMapping("/api/v1/info")
@RequiredArgsConstructor
public class InfoController {
    private final InfoService infoService;

    @GetMapping("/transaction")
    public ResponseEntity<?> getTransactionById(@RequestParam(value = "id", required = true) Long id) {
        return ResponseEntity.ok(infoService.findTransactionById(id));
    }

    @PostMapping("/tranByExtId")
    public ResponseEntity<?> getTransactionByExtId(@RequestBody @Valid TranCheckRequestDTO requestDTO) {
        return ResponseEntity.ok(infoService.findTransactionByInternalId(requestDTO));
    }
}
