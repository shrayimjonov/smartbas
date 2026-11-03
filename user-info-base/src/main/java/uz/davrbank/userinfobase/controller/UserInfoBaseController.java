package uz.davrbank.userinfobase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.userinfobase.dto.pagination.PaginationRequest;
import uz.davrbank.userinfobase.dto.pagination.PaginationResponse;
import uz.davrbank.userinfobase.dto.request.ResultResponse;
import uz.davrbank.userinfobase.dto.request.nci.LoanByPinflRequest;
import uz.davrbank.userinfobase.dto.request.nci.LoanInfoDto;
import uz.davrbank.userinfobase.dto.request.nci.LoanRequest;
import uz.davrbank.userinfobase.dto.request.nci.PersonDetailsRequest;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.nci.LoanByPinflResponse;
import uz.davrbank.userinfobase.dto.response.nci.LoanResponse;
import uz.davrbank.userinfobase.dto.response.nci.PersonDetailsResponse;
import uz.davrbank.userinfobase.service.nci.PersonDetailsService;

import java.util.List;


@RestController
@RequestMapping(value = "/api/loan", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole(T(uz.davrbank.userinfobase.enums.Permission).FILE_M_FILE_UP_DOWN)")
@Tag(name = "Loan Controller", description = "Endpoints for retrieving user and loan information")
public class UserInfoBaseController {

    private final PersonDetailsService personDetailsService;

    public UserInfoBaseController(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @PostMapping("/user-details")
    @Operation(
            summary = "Get user details",
            description = "Returns detailed information about a user by provided parameters",
            operationId = "getUserDetails"
    )
    public ResponseEntity<PersonDetailsResponse> getUserDetails(
            @RequestBody PersonDetailsRequest request) {

        var result = personDetailsService.getUserDetails(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/loan-by-pinfl")
    @Operation(
            summary = "Get loans by PINFL",
            description = "Returns loan information for a user using PINFL",
            operationId = "getLoanByPinfl"
    )
    public ResponseEntity<LoanByPinflResponse> getLoanByPinfl(
            @RequestBody LoanByPinflRequest request) {

        var result = personDetailsService.getLoanByPinfl(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Returns paginated list of users filtered by PINFL or loanId",
            operationId = "findAllUsers"
    )
    public ResponseEntity<ResultData<PaginationResponse<ResultResponse>>> findAll(
            @Parameter(description = "Filter by PINFL")
            @RequestParam(value = "filter", required = false) String pinfl,

            @Parameter(description = "Filter by loan ID")
            @RequestParam(value = "loanId", required = false) String loanId,

            PaginationRequest request) {

        var result = personDetailsService.getAllUsers(pinfl, loanId, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Returns user information by id",
            operationId = "findUserById"
    )
    public ResponseEntity<ResultData<ResultResponse>> findById(
            @Parameter(description = "User record id")
            @PathVariable Long id) {

        var result = personDetailsService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pinfl")
    @Operation(
            summary = "Get loans by PINFL",
            description = "Returns list of loans for a user by PINFL",
            operationId = "getUserLoans"
    )
    public ResponseEntity<ResultData<List<LoanInfoDto>>> getUserLoans(
            @Parameter(description = "User PINFL")
            @RequestParam String pinfl) {

        var result = personDetailsService.getUserLoans(pinfl);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/loan-info")
    @Operation(
            summary = "Get loan information",
            description = "Returns detailed loan information",
            operationId = "getLoanInfo"
    )
    public ResponseEntity<LoanResponse> getLoanInfo(
            @RequestBody LoanRequest request) {

        var response = personDetailsService.getLoanInfo(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process-csv")
    @Operation(
            summary = "Process CSV file",
            description = "Uploads CSV file containing PINFL list and returns user details",
            operationId = "processCsv"
    )
    public ResponseEntity<?> processCsv(
            @Parameter(description = "CSV file with PINFL values")
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is required");
        }

        try {
            List<PersonDetailsResponse> result = personDetailsService.processPinflsFromCsv(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing file: " + e.getMessage());
        }
    }
}