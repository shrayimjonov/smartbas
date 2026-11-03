package humo.integration.card.Visa.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataHistoryResp {

    public String B_DATE;

    public String STATUS;

    public String R_E_MAILS;

    public String CLIENT_B;

    public String R_MOB_PHONE;

    public String R_STREET;

    public String CLIENT;
}
