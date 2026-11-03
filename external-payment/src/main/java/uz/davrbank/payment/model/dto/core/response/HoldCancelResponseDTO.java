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
public class HoldCancelResponseDTO {
    private Long tranId;
    private BigDecimal amount;
    private String account;
    private ResponseStatus status;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private String username;

    public HoldCancelResponseDTO(Transaction transaction) {
        this.tranId = transaction.getId();
        this.amount = transaction.getAmount();
        this.account = transaction.getAccount();
        this.status = transaction.getStatus();
        this.createdDateTime = transaction.getCreatedTime();
        this.updatedDateTime = transaction.getUpdatedTime();
        this.username = transaction.getCreatedBy();

    }
}
