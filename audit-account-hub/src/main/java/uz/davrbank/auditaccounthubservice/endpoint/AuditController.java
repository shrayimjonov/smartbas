package uz.davrbank.auditaccounthubservice.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.auditaccounthubservice.aggregate.dto.AccountSummaryDTO;
import uz.davrbank.auditaccounthubservice.aggregate.dto.ResponseUniversalDTO;
import uz.davrbank.auditaccounthubservice.aggregate.dto.TransactionDTO;
import uz.davrbank.auditaccounthubservice.service.cases.AuditInterface;
import uz.davrbank.auditaccounthubservice.service.cases.AuditPaginationInterface;

import java.util.List;

@RestController
@RequestMapping("/audits")
public class AuditController {

    private final AuditInterface auditInterface;
    private final AuditPaginationInterface auditPaginationInterface;

    public AuditController(AuditInterface auditInterface, AuditPaginationInterface auditPaginationInterface) {
        this.auditInterface = auditInterface;
        this.auditPaginationInterface = auditPaginationInterface;
    }

    //---------------------------audit pagination ---------------------------

    @PostMapping("/pagination")
    public ResponseEntity<ResponseUniversalDTO<List<AccountSummaryDTO<TransactionDTO>>>> getData(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String iAccCod,
            @RequestParam String iBranch,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(auditPaginationInterface.getAuditPagination(startDate, endDate, page, size, iAccCod, iBranch));
    }
}