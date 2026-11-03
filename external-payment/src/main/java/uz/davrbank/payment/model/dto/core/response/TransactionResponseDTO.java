package uz.davrbank.payment.model.dto.core.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.payment.model.entity.Transaction;
import uz.nodir.commondto.model.enums.ResponseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {
    private Long tranId;
    private String username;
    private String account;
    private String internalId;
    private BigDecimal amount;
    private String refNum;
    private ResponseStatus status;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;


    public TransactionResponseDTO(Transaction transaction) {
        this.tranId = transaction.getId();
        this.username = transaction.getCreatedBy();
        this.account = transaction.getAccount();
        this.internalId = transaction.getInternalId();
        this.amount = transaction.getAmount();
        this.refNum = transaction.getRrn();
        this.status = transaction.getStatus();
        this.createdDateTime = transaction.getCreatedTime();
        this.updatedDateTime = transaction.getUpdatedTime();
    }
}
