package uz.davrbank.partnervalidationservice.merchant.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.partnervalidationservice.common.api.MessageResponse;
import uz.davrbank.partnervalidationservice.common.api.ResponseDTO;
import uz.davrbank.partnervalidationservice.common.api.ResponsePageDTO;
import uz.davrbank.partnervalidationservice.constants.ApiConstants;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantDetailDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantListDTO;
import uz.davrbank.partnervalidationservice.merchant.enums.MerchantStatus;
import uz.davrbank.partnervalidationservice.merchant.filter.MerchantFilter;
import uz.davrbank.partnervalidationservice.merchant.service.MerchantService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.MERCHANTS_ROOT_API)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MerchantController {

    MerchantService merchantService;

    @GetMapping
    public ResponseEntity<ResponseDTO<ResponsePageDTO<MerchantListDTO>>> page(@ParameterObject MerchantFilter filter) {
        return ResponseEntity.ok(ResponseDTO.ok(merchantService.getPageable(filter)));
    }

    @GetMapping(ApiConstants.LOOKUP)
    public ResponseEntity<ResponseDTO<java.util.List<MerchantListDTO>>> lookup(@ParameterObject MerchantFilter filter) {
        return ResponseEntity.ok(ResponseDTO.ok(merchantService.lookup(filter)));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<ResponseDTO<MerchantDetailDTO>> get(@PathVariable UUID id) {
        return ResponseEntity.ok(ResponseDTO.ok(merchantService.get(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<UUID>> create(@Valid @RequestBody MerchantDTO request) {
        return ResponseEntity.ok(ResponseDTO.ok(merchantService.create(request)));
    }

    @PutMapping(ApiConstants.ID)
    public ResponseEntity<ResponseDTO<UUID>> update(@PathVariable UUID id, @Valid @RequestBody MerchantDTO request) {
        return ResponseEntity.ok(ResponseDTO.ok(merchantService.update(id, request)));
    }

    @PatchMapping(ApiConstants.ID)
    public ResponseEntity<ResponseDTO<MessageResponse>> updateStatus(@PathVariable UUID id, @RequestParam MerchantStatus status) {
        merchantService.updateStatus(id, status);
        return ResponseEntity.ok(ResponseDTO.ok(MessageResponse.builder()
                .message("OK")
                .build()));
    }
}
