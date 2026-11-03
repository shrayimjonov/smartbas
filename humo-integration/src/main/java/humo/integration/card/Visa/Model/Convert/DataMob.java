package humo.integration.card.Visa.Model.Convert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonDeserialize(using = MyDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataMob {
    @JsonProperty("details")
    public ArrayList<Details> details;


    @Data
    public static class Details {
        @JsonProperty("REC_DATE")
        public String rEC_DATE;
        @JsonProperty("NATIONALITY")
        public String nATIONALITY;
        @JsonProperty("SURNAME")
        public String sURNAME;
        @JsonProperty("CLIENT_B")
        public String cLIENT_B;
        @JsonProperty("CTIME")
        public String cTIME;
        @JsonProperty("R_STREET")
        public String r_STREET;
        @JsonProperty("CLIENT")
        public String cLIENT;
        @JsonProperty("B_DATE")
        public String b_DATE;
        @JsonProperty("STATUS")
        public String sTATUS;
        @JsonProperty("R_PCODE")
        public String r_PCODE;
        @JsonProperty("R_CNTRY")
        public String r_CNTRY;
        @JsonProperty("R_E_MAILS")
        public String r_E_MAILS;
        @JsonProperty("CLN_CAT")
        public String cLN_CAT;
        @JsonProperty("F_NAMES")
        public String f_NAMES;
        @JsonProperty("R_MOB_PHONE")
        public String r_MOB_PHONE;
        @JsonProperty("BANK_C")
        public String bANK_C;
        @JsonProperty("U_CODE1")
        public String u_CODE1;
        @JsonProperty("COMENT")
        public String cOMENT;
        @JsonProperty("R_CITY")
        public String r_CITY;
        @JsonProperty("R_FAX")
        public String r_FAX;
    }
}
