package uz.davrbank.zalogipatekaservice.service.cases;

import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity<?> uploadReportToMinio(
            String startDate,
            String endDate,
            Integer cadastreType,
            Integer state,
            String branch,
            Integer loanId,
            String clientId,
            String pinfl,
            String clientName,
            String cadastreNum,
            String userName
    );
}
