package uz.davrbank.payment.model.dto.core.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.nodir.commondto.model.enums.Processing;
import uz.nodir.commondto.model.enums.ResponseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayResponseDTO {
    private Long tranId;
    private String paymentId;
    private String pan;
    private String holder;
    private Processing processing;
    private String refNum;
    private BigDecimal amount;
    private String recipient;
    private String account;
    private ResponseStatus status;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private String username;
}
