package uz.davrbank.zalogipatekaservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.zalogipatekaservice.service.cases.ReportService;




@RestController
@RequestMapping("v1/reports")
public class ZalogIpotekaFileController {

    private final ReportService reportService;

    public ZalogIpotekaFileController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/download/excel")
    public ResponseEntity<?> downloadReport(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam("cadastreType") Integer cadastreType,
            @RequestParam("state") Integer state,
            @RequestParam("branch") String branch,
            @RequestParam(value = "loanId", required = false) Integer loanId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "pinfl", required = false) String pinfl,
            @RequestParam(value = "clientName", required = false) String clientName,
            @RequestParam(value = "cadastreNum", required = false) String cadastreNum,
            @RequestHeader(value = "username", required = false) String fullName
    ) {
        return reportService.uploadReportToMinio(
                startDate,
                endDate,
                cadastreType,
                state,
                branch,
                loanId,
                clientId,
                pinfl,
                clientName,
                cadastreNum,
                fullName
        );
    }
}
