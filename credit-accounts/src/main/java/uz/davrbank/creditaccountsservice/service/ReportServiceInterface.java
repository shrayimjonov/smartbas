package uz.davrbank.creditaccountsservice.service;

import org.springframework.http.ResponseEntity;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditAccountRequestDTO;

public interface ReportServiceInterface {
    ResponseEntity<?> uploadReportToMinio(
            CreditAccountRequestDTO creditAccountRequestDTO, String userName
    );
}