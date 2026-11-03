package uz.davrbank.partnervalidationservice.logging.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.partnervalidationservice.common.api.ResponseDTO;
import uz.davrbank.partnervalidationservice.common.api.ResponsePageDTO;
import uz.davrbank.partnervalidationservice.constants.ApiConstants;
import uz.davrbank.partnervalidationservice.logging.dto.PartnerValidationLogListDTO;
import uz.davrbank.partnervalidationservice.logging.filter.PartnerValidationLogFilter;
import uz.davrbank.partnervalidationservice.logging.service.PartnerValidationLogService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.LOGS_ROOT_API)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PartnerValidationLogController {

    PartnerValidationLogService partnerValidationLogService;

    @GetMapping
    public ResponseEntity<ResponseDTO<ResponsePageDTO<PartnerValidationLogListDTO>>> page(
            @ParameterObject PartnerValidationLogFilter filter
    ) {
        return ResponseEntity.ok(ResponseDTO.ok(partnerValidationLogService.getPageable(filter)));
    }
}
