package humo.integration.card.Visa.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListAccountsReq {
    private String account;
    @Builder.Default
    private String subSystem = "VISA_MOBILE_BACK";
}
