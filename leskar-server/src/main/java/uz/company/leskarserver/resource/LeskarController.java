package uz.company.leskarserver.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.company.leskarserver.leskarintegration.*;
import uz.company.leskarserver.leskarintegration.base.LeskarBaseRequest;
import uz.company.leskarserver.leskarintegration.base.LeskarBaseResponse;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class LeskarController {

    @PostMapping("/transfer/agent/auth")
    public ResponseEntity<LeskarBaseResponse<LeskarLoginResponse>> login(@RequestBody LeskarLoginRequest request) {
        return ResponseEntity.ok(new LeskarBaseResponse<>("signature", true, "2025-09-24T15:45:08+00:00", new LeskarLoginResponse("REDACTED_JWT")));
    }

    @PostMapping("/transfer/countries")
    public ResponseEntity<LeskarBaseResponse<LeskarGetCountryListResponse>> getCountries() {
        return ResponseEntity.ok(new LeskarBaseResponse<>("signature", true, "2025-09-24T15:45:08+00:00", new LeskarGetCountryListResponse("uz", List.of(new AvailableTypesItem("source_currency", "target_currency", "CARD", new Limits(322, 45564))))));
    }

    @PostMapping("/transfer/fees")
    public ResponseEntity<LeskarBaseResponse<LeskarGetFeeResponse>> getFees(@RequestBody LeskarBaseRequest<LeskarGetFeeRequest> request) {
        return ResponseEntity.ok(new LeskarBaseResponse<>("signature", true, "2025-09-24T15:45:08+00:00", new LeskarGetFeeResponse("usd", new RequiredFields(List.of(), List.of()), List.of(new FeesItem(BigDecimal.valueOf(23), "usd", "card", BigDecimal.valueOf(54))), BigDecimal.valueOf(432), "uzs", BigDecimal.valueOf(322))));
    }

    @PostMapping("/transfer/person/create")
    public ResponseEntity<LeskarBaseResponse<LeskarPersonCreateResponse>> createPerson(@RequestBody LeskarBaseRequest<LeskarEncyptedDataPayload> request) {
        return ResponseEntity.ok(new LeskarBaseResponse<>("signature", true, "2025-09-24T15:45:08+00:00", new LeskarPersonCreateResponse("wewerf;sldkg")));
    }
}
