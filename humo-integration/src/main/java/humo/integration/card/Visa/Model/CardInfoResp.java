package humo.integration.card.Visa.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardInfoResp {
    @JsonProperty("CARD_SUFIKS")
    public String cARD_SUFIKS;
    @JsonProperty("RENEW_DATE")
    public Date rENEW_DATE;
    @JsonProperty("RENEW1_DATE")
    public Date rENEW1_DATE;
    @JsonProperty("RENEWED_CARD")
    public String rENEWED_CARD;
    @JsonProperty("CARD_NAME")
    public String cARD_NAME;
    @JsonProperty("CTIME")
    public Date cTIME;
    @JsonProperty("M_NAME")
    public String m_NAME;
    @JsonProperty("CARD_NUM")
    public String cARD_NUM;
    @JsonProperty("CARD")
    public String cARD;
}
