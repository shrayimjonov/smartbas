package uz.davrbank.payment.model.dto.core.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.payment.model.enums.CardStatus;
import uz.nodir.commondto.model.enums.Processing;

/**
 * Card response DTO with status field
 * Extends CardResponseV2DTO fields and adds card status
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardWithStatusDTO {
    private String card;
    private String maskedPan;
    private Processing processing;
    private Long balance;
    private String fullName;
    private Boolean sms;
    private String phone;
    private String token;
    private CardStatus status;

    public boolean isActiveAndNotBlocked() {
        return status != null && status.isActiveAndNotBlocked();
    }

    public boolean isBlocked() {
        return status != null && status.isBlocked();
    }
}
