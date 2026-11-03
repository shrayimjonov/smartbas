package soap.humo_gateway.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soap.humo_gateway.dto.request.*;
import soap.humo_gateway.service.ActionService;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/action")
public class ActionController {
    private final ActionService actionService;

    @PostMapping("/p2p")
    public ResponseEntity<?> p2pH2H(@RequestBody @Valid P2pRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(actionService.p2p(servletRequest, request));
    }

    @PostMapping("/debitCreate")
    public ResponseEntity<?> debitCreate(@RequestBody @Valid DebitCreateRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(actionService.debitCreate(servletRequest, request));
    }

    @PostMapping("/debitFinish")
    public ResponseEntity<?> debitFinish(@RequestBody @Valid DebitConfirmRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(actionService.debitConfirm(servletRequest, request));
    }

    @PostMapping("/credit")
    public ResponseEntity<?> credit(@RequestBody @Valid CreditRequest request, ServletRequest servletRequest) {
       return ResponseEntity.ok(actionService.credit(servletRequest, request));
    }


    @PostMapping("/assignPin")
    public ResponseEntity<?> assignPin(@RequestBody @Valid PinAssignRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(actionService.pinAssign(servletRequest, request));
    }

    @PostMapping("/execute/transaction")
    public ResponseEntity<?> executeTransaction(@RequestBody @Valid ExecuteTransactionRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(actionService.executeTransaction(servletRequest, request));
    }
}
