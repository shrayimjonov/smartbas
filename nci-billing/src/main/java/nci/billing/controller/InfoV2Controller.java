package nci.billing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nci.billing.model.dto.DebtsDto;
import nci.billing.model.dto.LoanDto;
import nci.billing.model.request.TransactionRequest;
import nci.billing.service.InfoV2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/info")
@RequiredArgsConstructor
public class InfoV2Controller {
    private final InfoV2Service infoV2Service;

    @PostMapping("/client")
    public ResponseEntity<?> getClientDataByLoanId(@RequestBody @Valid LoanDto dto) {
        return ResponseEntity.ok(infoV2Service.getClientInfo(dto));
    }

    @PostMapping("/loan")
    public ResponseEntity<?> getClientDebtInfoByLoanId(@RequestBody @Valid LoanDto dto) {
        return ResponseEntity.ok(infoV2Service.getLoanInfoV2(dto));
    }

    @PostMapping("/debts")
    public ResponseEntity<?> getClientDebtInfoByLoanId(@RequestBody @Valid DebtsDto dto) {
        return ResponseEntity.ok(infoV2Service.getDebtsByBranch(dto));
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> getLoanTransactions(@RequestBody @Valid TransactionRequest request) {
        return ResponseEntity.ok(infoV2Service.getLoanTransactions(request));
    }

    @GetMapping("/course")
    public ResponseEntity<?> getCourse() {
        return ResponseEntity.ok(infoV2Service.getCourse());
    }

    @GetMapping("/cardsByPersonCode")
    public ResponseEntity<?> getCardsByPersonCode(@RequestParam("person_code") String personCode) {
        return ResponseEntity.ok(infoV2Service.getCardsByPersonCode(personCode));
    }

    @GetMapping("/clientInfoByPINFL")
    public ResponseEntity<?> getClientInfoByPINFL(@RequestParam("person_code") String personCode) {
        return ResponseEntity.ok(infoV2Service.getClientInfoByPINFL(personCode));
    }
}
