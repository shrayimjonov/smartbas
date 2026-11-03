package uz.davrbank.yeisvoservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.yeisvoservice.aggregate.dto.request.ContractListRequestBody;
import uz.davrbank.yeisvoservice.aggregate.dto.request.ContractRequestBody;
import uz.davrbank.yeisvoservice.aggregate.dto.request.TestDtoReq;
import uz.davrbank.yeisvoservice.aggregate.dto.response.*;
import uz.davrbank.yeisvoservice.service.service.sevices.ContractService;

@RestController
@RequestMapping("/api/v1/contract")
@RequiredArgsConstructor
public class ContractController {
        private final ContractService contractService;

        @PostMapping("/check")
        public ResponseEntity<TestDtoRes> checkContract(@RequestBody TestDtoReq request) {
            return ResponseEntity.ok(contractService.checkContract(request));
        }

        @PostMapping("/status")
        public ResponseEntity<ContractGetStatusRes> getContractStatus(@RequestBody ContractRequestBody request) {
            return ResponseEntity.ok(contractService.getContractStatus(request));
        }

        @PostMapping("/update/details")
        public ResponseEntity<ContractGetRes> updateContractDetails(@RequestBody ContractRequestBody request) {
            return ResponseEntity.ok(contractService.updateContractDetails(request));
        }

        @PostMapping("/list")
        public ResponseEntity<ContractGetListWithPageResponse> getContractList(@RequestBody ContractListRequestBody request) {
            return ResponseEntity.ok(contractService.getContractList(request));
        }

        @PostMapping("/owner/docs")
        public ResponseEntity<ContractGetOwnerDocRes> getContractOwnerDocs(@RequestBody ContractRequestBody request) {
            return ResponseEntity.ok(contractService.getContractOwnerDocs(request));
        }

        @PostMapping("/history")
        public ResponseEntity<ContractGetHistoryRes> getHistoryDocs(@RequestBody ContractRequestBody request) {
            return ResponseEntity.ok(contractService.getHistoryDocs(request));
        }

        @PostMapping("/get")
        public ResponseEntity<ContractGetRes> getContract(@RequestBody ContractRequestBody request) {
            return ResponseEntity.ok(contractService.getContract(request));
        }

        @PostMapping("/bank/docs")
        public ResponseEntity<ContractGetBankDocsRes> getBankDocs(@RequestBody ContractRequestBody request) {
            return ResponseEntity.ok(contractService.getBankDocs(request));
        }
}
