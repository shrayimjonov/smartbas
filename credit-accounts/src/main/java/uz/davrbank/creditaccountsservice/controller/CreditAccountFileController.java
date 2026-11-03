package uz.davrbank.creditaccountsservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditAccountRequestDTO;
import uz.davrbank.creditaccountsservice.service.ReportServiceInterface;

@RestController
@RequestMapping("v1/reports")
public class CreditAccountFileController {
    private final ReportServiceInterface reportServiceInterface;

    public CreditAccountFileController(ReportServiceInterface reportServiceInterface) {
        this.reportServiceInterface = reportServiceInterface;
    }

    @PostMapping("/download/excel")
    public ResponseEntity<?> downloadExcelFile(@RequestBody CreditAccountRequestDTO creditAccountRequestDTO,
                                               @RequestHeader(value = "username", required = false) String userName)
    {
        return reportServiceInterface.uploadReportToMinio(creditAccountRequestDTO, userName );

    }
}
