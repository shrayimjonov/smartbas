package humo.integration.card.Visa.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListCustomersResp {
    @JsonProperty("DOC_NAME")
    public String dOC_NAME;
    @JsonProperty("SERIAL_NO")
    public String sERIAL_NO;
    @JsonProperty("CLIENT_B")
    public String cLIENT_B;
    @JsonProperty("ISSUED_BY")
    public String iSSUED_BY;
    @JsonProperty("DOC_SINCE")
    public Date dOC_SINCE;
    @JsonProperty("CARD")
    public String cARD;
    @JsonProperty("SUPPLEMENTARY_CVV2")
    public String sUPPLEMENTARY_CVV2;
    @JsonProperty("NOTES")
    public String nOTES;
    @JsonProperty("EMP_NAME")
    public String eMP_NAME;
    @JsonProperty("STATUS")
    public String sTATUS;
    @JsonProperty("DOC_TYPE")
    public String dOC_TYPE;
    @JsonProperty("R_CNTRY")
    public String r_CNTRY;
    @JsonProperty("A3VTS_COUNTY")
    public String a3VTS_COUNTY;
    @JsonProperty("R_E_MAILS")
    public String r_E_MAILS;
    @JsonProperty("F_NAMES")
    public String f_NAMES;
    @JsonProperty("R_MOB_PHONE")
    public String r_MOB_PHONE;
    @JsonProperty("A3VTS_FLAG2")
    public String a3VTS_FLAG2;
    @JsonProperty("BANK_C")
    public String bANK_C;
    @JsonProperty("A3VTS_FLAG1")
    public String a3VTS_FLAG1;
    @JsonProperty("SUPPLEMENTARY_EXPIRY")
    public String sUPPLEMENTARY_EXPIRY;
    @JsonProperty("SUPPLEMENTARY_PAN")
    public String sUPPLEMENTARY_PAN;
    @JsonProperty("SURNAME")
    public String sURNAME;
    @JsonProperty("U_COD1")
    public String u_COD1;
    @JsonProperty("PERSON_CODE")
    public String pERSON_CODE;
    @JsonProperty("R_STREET")
    public String r_STREET;
    @JsonProperty("CLIENT")
    public String cLIENT;
    @JsonProperty("B_DATE")
    public Date b_DATE;
    @JsonProperty("R_PCODE")
    public String r_PCODE;
    @JsonProperty("ID_CARD")
    public String iD_CARD;
    @JsonProperty("R_CITY")
    public String r_CITY;
}
