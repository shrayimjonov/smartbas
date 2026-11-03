package soap.humo_gateway.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soap.humo_gateway.dto.request.limits.GetCardLimitBalanceRequest;
import soap.humo_gateway.dto.request.limits.GetCardLimitRequest;
import soap.humo_gateway.dto.request.limits.GetLimits;
import soap.humo_gateway.dto.request.limits.SetCardLimitRequest;
import soap.humo_gateway.service.LimitsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/limit")
@RequiredArgsConstructor
public class LimitController {
    private final LimitsService limitsService;

    @PostMapping("/types")
    public ResponseEntity<?> getLimitsType(@RequestBody @Valid GetLimits request, HttpServletRequest servletRequest) {
        return ResponseEntity.ok(limitsService.getLimitsType(request, servletRequest));
    }

    @PostMapping("/card")
    public ResponseEntity<?> getCardLimits(@RequestBody @Valid GetCardLimitRequest request, HttpServletRequest servletRequest) {
        return ResponseEntity.ok(limitsService.getCardLimits(request, servletRequest));
    }

    @PutMapping("/card")
    public ResponseEntity<?> setCardLimits(@RequestBody @Valid SetCardLimitRequest request, HttpServletRequest servletRequest) {
        return ResponseEntity.ok(limitsService.setCardLimit(request, servletRequest));
    }

    @PostMapping("/balance")
    public ResponseEntity<?> getCardLimitsBalance(@RequestBody @Valid GetCardLimitBalanceRequest request, HttpServletRequest servletRequest) {
        return ResponseEntity.ok(limitsService.getCardLimitsBalance(request, servletRequest));
    }

    @PostMapping("/v2/card")
    public ResponseEntity<?> getCardLimitsV2(@RequestBody @Valid GetCardLimitRequest request) {
        return ResponseEntity.ok(limitsService.getCardLimitsV2(request));
    }


}
