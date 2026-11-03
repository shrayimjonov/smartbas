package uz.davrbank.bustreasury.aggregate.dto.response.cbu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyRateResponseDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("Code")
    private String code;

    @JsonProperty("Ccy")
    private String ccy;

    @JsonProperty("Rate")
    private String rate;

    @JsonProperty("CcyNm_EN")
    private String ccyNmEN;

    @JsonProperty("CcyNm_UZC")
    private String ccyNmUZC;

    @JsonProperty("CcyNm_UZ")
    private String ccyNmUZ;

    @JsonProperty("CcyNm_RU")
    private String ccyNmRU;

    @JsonProperty("Nominal")
    private String nominal;

    @JsonProperty("Diff")
    private String diff;

    @JsonProperty("Date")
    private String date;
}
