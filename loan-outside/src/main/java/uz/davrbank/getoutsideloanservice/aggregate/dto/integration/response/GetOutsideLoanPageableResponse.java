package uz.davrbank.getoutsideloanservice.aggregate.dto.integration.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetOutsideLoanPageableResponse {

    @JsonProperty("STATUS")
    String status;

    @JsonProperty("BRANCH")
    String branch;

    @JsonProperty("ID")
    Long id;

    @JsonProperty("ACC_BAL")
    String accBal;

    @JsonProperty("CURRENCY")
    String currency;

    @JsonProperty("CLIENT")
    String client;

    @JsonProperty("ACCOUNT")
    String account;

    @JsonProperty("METHOD_NACH")
    String methodNach;

    @JsonProperty("NAME")
    String name;

    @JsonProperty("NUM_DOG")
    String numDog;

    @JsonProperty("S_IN_SUM")
    BigDecimal sInSum;

    @JsonProperty("DT_SUM")
    BigDecimal dtSum;

    @JsonProperty("CT_SUM")
    BigDecimal ctSum;

    @JsonProperty("S_OUT_SUM")
    BigDecimal sOutSum;

    @JsonProperty("S_IN")
    BigDecimal sIn;

    @JsonProperty("DT")
    BigDecimal dt;

    @JsonProperty("CT")
    BigDecimal ct;

    @JsonProperty("S_OUT")
    BigDecimal sOut;

    @JsonProperty("ACC_PROS")
    String accPros;

    @JsonProperty("DATE_BEG")
    String dateBeg;

    @JsonProperty("DATE_END")
    String dateEnd;

    @JsonProperty("RATE")
    BigDecimal rate;

    @JsonProperty("OSV_OSN_DOLG")
    List<LoanScheduleItemResponse> osvOsnDolg;

    @JsonProperty("POG_OSN_DOLG")
    List<LoanScheduleItemResponse> pogOsnDolg;

    @JsonProperty("PROC_ZADOL")
    List<LoanScheduleItemResponse> procZadol;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoanScheduleItemResponse {

        @JsonProperty("DATE_POG")
        String datePog;

        @JsonProperty("SUMMA_POG")
        BigDecimal summaPog;

        @JsonProperty("NUM_POG")
        String numPog;

        @JsonProperty("STATUS_POG")
        Integer statusPog;
    }
}
