package uz.davrbank.auditaccounthubservice.service.cases;

import uz.davrbank.auditaccounthubservice.aggregate.dto.AccountSummaryDTO;
import uz.davrbank.auditaccounthubservice.aggregate.dto.ResponseUniversalDTO;
import uz.davrbank.auditaccounthubservice.aggregate.dto.TransactionDTO;

import java.util.List;

public interface AuditPaginationInterface {

    ResponseUniversalDTO<List<AccountSummaryDTO<TransactionDTO>>> getAuditPagination(String startDate, String endDate, Integer pageNo, Integer pageSize, String iAccKod, String branch);

}
