package humo.integration.card.Visa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListAccountsResp {
    @JsonProperty("SURNAME")
    private String sURNAME;
    @JsonProperty("CLIENT_B")
    private String cLIENT_B;
    @JsonProperty("ACC_PRTY")
    private String aCC_PRTY;
    @JsonProperty("TRANZ_ACCT")
    private String tRANZ_ACCT;
    @JsonProperty("MAIN_ROW")
    private String mAIN_ROW;
    @JsonProperty("B_BR_ID")
    private String b_BR_ID;
    @JsonProperty("CLIENT")
    private String cLIENT;
    @JsonProperty("STATUS")
    private String sTATUS;
    @JsonProperty("GROUPC")
    private String gROUPC;
    @JsonProperty("F_NAMES")
    private String f_NAMES;
    @JsonProperty("CARD_ACCT")
    private String cARD_ACCT;
    @JsonProperty("C_ACCNT_TYPE")
    private String c_ACCNT_TYPE;
    @JsonProperty("BANK_C")
    private String bANK_C;
    @JsonProperty("CCY")
    private String cCY;
    @JsonProperty("OFFICE_ID")
    private String oFFICE_ID;
    @JsonProperty("COND_SET")
    private String cOND_SET;
    @JsonProperty("ACCOUNT_NO")
    private String aCCOUNT_NO;
}
