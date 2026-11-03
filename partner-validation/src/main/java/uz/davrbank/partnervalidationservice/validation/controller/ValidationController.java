package uz.davrbank.partnervalidationservice.validation.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.partnervalidationservice.common.api.ResponseDTO;
import uz.davrbank.partnervalidationservice.constants.ApiConstants;
import uz.davrbank.partnervalidationservice.validation.dto.ValidationRequest;
import uz.davrbank.partnervalidationservice.validation.dto.ValidationResponse;
import uz.davrbank.partnervalidationservice.validation.service.PartnerValidationService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.VALIDATION_ROOT_API)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ValidationController {

    PartnerValidationService partnerValidationService;

    @PostMapping(ApiConstants.VALIDATE)
    public ResponseEntity<ResponseDTO<ValidationResponse>> validate(@Valid @RequestBody ValidationRequest request) {
        return ResponseEntity.ok(ResponseDTO.ok(partnerValidationService.validate(request)));
    }
}
