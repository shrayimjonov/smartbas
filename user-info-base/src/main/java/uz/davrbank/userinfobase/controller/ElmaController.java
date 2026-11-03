package uz.davrbank.userinfobase.controller;

import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.userinfobase.dto.request.elma.ElmaRequestDto;
import uz.davrbank.userinfobase.dto.request.elma.ElmaResponseDto;
import uz.davrbank.userinfobase.dto.request.elma.LoanIdAndBranchIdRequestDto;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.feign.elma.ElmaFeignClient;
import uz.davrbank.userinfobase.service.elma.ElmaService;

@RestController
@RequestMapping(value = "/api/elma", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ELMA Controller", description = "Integration endpoints with ELMA system")
public class ElmaController {

    private final ElmaFeignClient elmaFeignClient;
    private final ElmaService service;

    @PostMapping("/request")
    @Operation(
            summary = "Send request to ELMA",
            description = "Sends transfer request to ELMA via Feign client",
            operationId = "elmaTransferRequest"
    )
    public ResponseEntity<?> getElmaTransfer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ELMA transfer request payload",
                    required = true
            )
            @RequestBody ElmaRequestDto request) {
        try {
            var result = elmaFeignClient.transfer(request);
            return ResponseEntity.ok(result);
        } catch (FeignException f) {
            return ResponseEntity.badRequest().body(f.getMessage());
        }
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @Operation(
            summary = "Create ELMA transfer",
            description = "Creates transfer request to ELMA using loanId and branchId",
            operationId = "createElmaTransfer"
    )
    public ResponseEntity<ResultData<ElmaResponseDto>> requestToElma(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Loan and branch identifiers for ELMA transfer",
                    required = true
            )
            @RequestBody LoanIdAndBranchIdRequestDto requestDto) {

        var result = service.requestToElma(requestDto);
        return ResponseEntity.ok(result);
    }
}