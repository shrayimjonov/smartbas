package uz.davrbank.partnervalidationservice.validation.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GatewayMethod202Response(
        @JsonAlias({"ERROR_CODE", "KOD_ERR"})
        Integer errorCode,
        @JsonProperty("ERROR_NOTE")
        String errorNote,
        @JsonProperty("TRANS_ID")
        String transId,
        @JsonProperty("BRANCH")
        String branch,
        @JsonProperty("ID")
        String id,
        @JsonProperty("ACC_BAL")
        String accountBalance,
        @JsonProperty("CURRENCY")
        String currency,
        @JsonProperty("CLIENT")
        String client,
        @JsonProperty("ID_ORDER")
        String orderId,
        @JsonProperty("NAME")
        String name,
        @JsonProperty("SGN")
        String sgn,
        @JsonProperty("BAL")
        String balance,
        @JsonProperty("SIGN_REGISTR")
        Integer signRegistr,
        @JsonProperty("S_IN")
        Long amountIn,
        @JsonProperty("S_OUT")
        Long amountOut,
        @JsonProperty("DT")
        String dt,
        @JsonProperty("CT")
        Long ct,
        @JsonProperty("S_IN_TMP")
        Long amountInTmp,
        @JsonProperty("S_OUT_TMP")
        Long amountOutTmp,
        @JsonProperty("DT_TMP")
        String dtTmp,
        @JsonProperty("CT_TMP")
        Long ctTmp,
        @JsonProperty("L_DATE")
        String lastDate,
        @JsonProperty("DATE_OPEN")
        String dateOpen,
        @JsonProperty("DATE_CLOSE")
        String dateClose,
        @JsonProperty("ACC_GROUP_ID")
        Integer accountGroupId,
        @JsonProperty("STATE")
        Integer state
) {
}
