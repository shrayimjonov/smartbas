package uz.davrbank.zalogautoservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.zalogautoservice.service.AutoReportService;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
public class AutoReportFileController {

    private final AutoReportService reportService;

    @GetMapping("/excel")
    public ResponseEntity<?> downloadReport(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") String startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") String endDate,
            @RequestParam("state") Integer state,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "loanId", required = false) Integer loanId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "pinfl", required = false) String pinfl,
            @RequestParam(value = "clientName", required = false) String clientName,
            @RequestParam(value = "stateNumber", required = false) String stateNumber,
            @RequestParam(value = "address", required = false) String address,
            @RequestHeader(value = "username", required = false, defaultValue = "system") String fullName) {

        return reportService.uploadReportToMinio(
                startDate, endDate, state,
                branch, loanId, clientId, pinfl, clientName, stateNumber, address,
                fullName
        );
    }
}