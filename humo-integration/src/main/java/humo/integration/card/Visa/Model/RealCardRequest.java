package humo.integration.card.Visa.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RealCardRequest {
    private String bankC;
    private String groupC;
    private String card;
    @Builder.Default
    private String subSystem = "VISA_MOBILE_BACK";

    public String getBankC() {
        return bankC;
    }

    public void setBankC(String bankC) {
        this.bankC = bankC;
    }

    public String getGroupC() {
        return groupC;
    }

    public void setGroupC(String groupC) {
        this.groupC = groupC;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
