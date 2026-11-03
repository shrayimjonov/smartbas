package uz.davrbank.auditaccounthubservice.service.cases;

import uz.davrbank.auditaccounthubservice.aggregate.dto.AccountSummaryDTO;
import uz.davrbank.auditaccounthubservice.aggregate.dto.ResponseUniversalDTO;

public interface AuditInterface {

    ResponseUniversalDTO<AccountSummaryDTO> getData(String startDate, String endDate);
}