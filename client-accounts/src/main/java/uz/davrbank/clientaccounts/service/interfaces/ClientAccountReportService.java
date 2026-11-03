package uz.davrbank.clientaccounts.service.interfaces;

import org.springframework.http.ResponseEntity;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountRequestDto;

public interface ClientAccountReportService {
    ResponseEntity<?> downloadExcelReport(
            ClientAccountRequestDto dto,
            String username
    );
}