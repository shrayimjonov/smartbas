package uz.davrbank.userinfobase.controller;

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
import uz.davrbank.userinfobase.dto.request.dochub.LoanIdRequestDto;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.files.FileUploadResponse;
import uz.davrbank.userinfobase.service.dochub.DochubService;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/dochub", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Dochub Controller", description = "Endpoints for integration with Dochub document service")
public class DochubController {

    private final DochubService dochubService;

    @PostMapping(value = "/download", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
    @Operation(
            summary = "Download document from Dochub",
            description = "Requests document from Dochub service using loanId",
            operationId = "downloadDochubFile"
    )
    public ResponseEntity<ResultData<FileUploadResponse>> downloadFile(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Loan identifier request for downloading document",
                    required = true
            )
            @RequestBody LoanIdRequestDto loanIdRequestDto) {

        var result = dochubService.requestToDochub(loanIdRequestDto);
        return ResponseEntity.ok(result);
    }
}