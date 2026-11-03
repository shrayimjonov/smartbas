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
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantAccountDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantAccountDetailDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantAccountListDTO;
import uz.davrbank.partnervalidationservice.merchant.filter.MerchantAccountFilter;
import uz.davrbank.partnervalidationservice.merchant.service.MerchantAccountService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.MERCHANT_ACCOUNTS_ROOT_API)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MerchantAccountController {

    MerchantAccountService merchantAccountService;

    @GetMapping
    public ResponseEntity<ResponseDTO<ResponsePageDTO<MerchantAccountListDTO>>> page(
            @ParameterObject MerchantAccountFilter filter
    ) {
        return ResponseEntity.ok(ResponseDTO.ok(merchantAccountService.getPageable(filter)));
    }

    @GetMapping(ApiConstants.LOOKUP)
    public ResponseEntity<ResponseDTO<java.util.List<MerchantAccountListDTO>>> lookup(
            @ParameterObject MerchantAccountFilter filter
    ) {
        return ResponseEntity.ok(ResponseDTO.ok(merchantAccountService.lookup(filter)));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<ResponseDTO<MerchantAccountDetailDTO>> get(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(ResponseDTO.ok(merchantAccountService.get(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<UUID>> create(
            @Valid @RequestBody MerchantAccountDTO request
    ) {
        return ResponseEntity.ok(ResponseDTO.ok(merchantAccountService.create(request)));
    }

    @PutMapping(ApiConstants.ID)
    public ResponseEntity<ResponseDTO<UUID>> update(
            @PathVariable UUID id,
            @Valid @RequestBody MerchantAccountDTO request
    ) {
        return ResponseEntity.ok(ResponseDTO.ok(merchantAccountService.update(id, request)));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<ResponseDTO<MessageResponse>> delete(@PathVariable UUID id) {
        merchantAccountService.delete(id);
        return ResponseEntity.ok(ResponseDTO.ok(MessageResponse.builder()
                .message("Merchant account blocked successfully")
                .build()));
    }
}
