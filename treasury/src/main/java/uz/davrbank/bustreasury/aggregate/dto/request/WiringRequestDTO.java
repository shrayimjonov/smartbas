package uz.davrbank.bustreasury.aggregate.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WiringRequestDTO {

    @JsonProperty("debitMFO")
    private Long debitMFOId;

    @JsonProperty("debitAccountNum")
    @Pattern(regexp = "^\\d{20}$", message = "Debit account number must be exactly 20 digits")
    private String debitAccountNum;

    @JsonProperty("debitInn")
    @Pattern(regexp = "^\\d{9}$", message = "INN  must be exactly 9 digits")
    private String debitInn;

    @JsonProperty("debitComment")
    private String debitComment;

    @JsonProperty("creditMFO")
    private Long creditorMFOId;

    @JsonProperty("creditAccountNum")
    @Pattern(regexp = "^\\d{20}$", message = "Debit account number must be exactly 20 digits")
    private String creditAccountNum;

    @JsonProperty("creditInn")
    @Pattern(regexp = "^\\d{9}$", message = "INN  must be exactly 9 digits")
    private String creditInn;

    @JsonProperty("creditComment")
    private String creditComment;

    @JsonProperty("templateId")
    private Long templateId;

    @JsonProperty("docTypeId")
    private Long docTypeId;

    @JsonProperty("purposeId")
    private Long purposeId;

//    @JsonProperty("paymentid")
//    private Long paymentId;

    @JsonProperty("typePlateId")
    private Long typePlateId;

    @JsonProperty("createdBy")
    private String createdBy;
}
