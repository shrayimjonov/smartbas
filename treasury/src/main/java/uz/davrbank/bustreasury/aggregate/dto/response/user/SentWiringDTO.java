package uz.davrbank.bustreasury.aggregate.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.bustreasury.aggregate.model.user.SentWiring;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SentWiringDTO {

    private Long id;
    private String documentNumber;
    private String transId;
    private String errorCode;
    private String errorNote;
    private String finaleState;
    private LocalDateTime createdDate;

    private Long wiringId;
    private String purpose;
    private String typeDoc;
    private String debitAccount;
    private String debitInn;
    private String creditAccount;
    private String creditInn;
    private String createdBy;

    public static SentWiringDTO fromEntity(SentWiring s) {
        var w = s.getWiring();
        return new SentWiringDTO(
                s.getId(),
                s.getDocumentNumber(),
                s.getTransId(),
                s.getErrorCode(),
                s.getErrorNote(),
                s.getFinaleState(),
                s.getCreatedDate(),
                (w != null ? w.getWiringId() : null),
                (w != null && w.getPurpose() != null ? w.getPurpose().getPurpose() : null),
                (w != null && w.getDocType() != null ? w.getDocType().getKodDoc() : null),
                (w != null ? w.getDebitAccountNum() : null),
                (w != null ? w.getDebitInn() : null),
                (w != null ? w.getCreditAccountNum() : null),
                (w != null ? w.getCreditInn() : null),
                (w != null ? w.getCreatedBy() : null)
        );
    }
}
