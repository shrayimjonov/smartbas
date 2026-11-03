package soap.humo_gateway.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soap.humo_gateway.dto.request.*;
import soap.humo_gateway.service.InfoService;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class InfoController {
    private final InfoService infoService;


    @PostMapping("/story")
    public ResponseEntity<?> findHumoHistoryByCard(@RequestBody @Valid HistoryRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(infoService.humoHistory(servletRequest, request));
    }

    @PostMapping("/transit")
    public ResponseEntity<?> findTransitAccount(@RequestBody @Valid TransitRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(infoService.getCardTransitAccount(servletRequest, request));
    }

    @PostMapping("/balance")
    public ResponseEntity<?> getCardBalance(@RequestBody @Valid BalanceRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(infoService.getCardBalance(servletRequest, request));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateCard(@RequestBody @Valid DeactivateRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(infoService.deActivateCard(servletRequest, request));
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateCard(@RequestBody @Valid DeactivateRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(infoService.activateCard(servletRequest, request));
    }

    @PostMapping("/getPinCount")
    public ResponseEntity<?> getPinCount(@RequestBody @Valid PinCountReq request, ServletRequest servletRequest) {
        return ResponseEntity.ok(infoService.getPinCount(servletRequest, request));
    }

    @PostMapping("/resetPinCount")
    public ResponseEntity<?> resetPinCounter(@RequestBody @Valid ResetPinReq request, ServletRequest servletRequest) {
        return ResponseEntity.ok(infoService.resetPinCount(servletRequest, request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferDataToFo(@RequestBody @Valid TransferDataRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(infoService.transferDataToFo(servletRequest, request));
    }
}
