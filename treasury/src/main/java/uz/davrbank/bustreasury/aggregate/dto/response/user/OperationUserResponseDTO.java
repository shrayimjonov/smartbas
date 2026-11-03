package uz.davrbank.bustreasury.aggregate.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OperationUserResponseDTO {

    @JsonProperty("ERROR_CODE")
    private String errorCode;
    @JsonProperty("ERROR_NOTE")
    private String errorNote;
    @JsonProperty("TRANS_ID")
    private String transId;
    @JsonProperty("GROUP_ID")
    private Long groupId;

    @JsonProperty("TOTAL_WIRES")
    private Integer totalWires;

    @JsonProperty("APPROVED")
    private Integer approved;

    @JsonProperty("DELETED")
    private Integer deleted;

    @JsonProperty("FAILED")
    private Integer failed;

    @JsonProperty("TIMEOUT")
    private Integer timeout;

    @JsonProperty("STATUS_SUMMARY")
    private String statusSummary;
}
