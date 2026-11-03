package nci.billing.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class PayResponse {
    private Long ofPrincipal;
    private Long ofOverdueDebt;
    private Long ofInterestOnTerm;
    private Long ofOverdueInterest;
    private Long forFines;
    private String request_id;
    private String genId;
}
