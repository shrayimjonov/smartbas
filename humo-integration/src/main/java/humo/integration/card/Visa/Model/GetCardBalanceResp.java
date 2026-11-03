package humo.integration.card.Visa.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCardBalanceResp {

    public String CARD_ACCT;

    public String AVAIL_AMOUNT;

    public String C_ACCNT_TYPE;

    public String CCY;

    public String LOCKED_AMOUNT;

    public String ACCOUNT_NO;

    public String END_BAL;

    public String CARD_LAST_ACTIV_DATE;

    public String EXPIRY1;

    public String CARD_STATUS;

    public String ACC_STATUS;

    public String CARD;
}
