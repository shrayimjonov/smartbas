package uz.davrbank.creditaccountsservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditAccountRequestDTO;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditRequestPageable;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditRequestSorted;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponseDTO;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponsePaginationDTO;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponseSortDTO;
import uz.davrbank.creditaccountsservice.service.CreditAccountInterface;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
public class CreditAccountController {
    private final CreditAccountInterface creditAccountInterface;

        @PostMapping("/get/all")
        public ResponseEntity<ResponseDTO> getAllCreditAccounts(
                @Valid @RequestBody CreditAccountRequestDTO request) {

            return ResponseEntity.ok(
                    creditAccountInterface.getAllCreditAccounts(request)
            );
        }

        @PostMapping("/get/all/pageable")
        public ResponseEntity<ResponsePaginationDTO> getAllCreditAccountsPageable(
                @Valid @RequestBody CreditRequestPageable request) {

            return ResponseEntity.ok(
                    creditAccountInterface.getAllCreditAccountsPageable(request)
            );
        }

        @PostMapping("/get/all/pageable/sorted")
        public ResponseEntity<ResponseSortDTO> getAllCreditAccountsPageableSorted(
                @Valid @RequestBody CreditRequestSorted request) {

            return ResponseEntity.ok(
                    creditAccountInterface.getAllCreditAccountsPageableSorted(request)
            );
        }}

