package humo.integration.card.Model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryTransHistResp {
    @JsonProperty("Details")
    public Details details;

    @Data
    public static class Details {
        @JsonProperty("row")
        public ArrayList<Row> row;
    }

    @Data
    public static class Row {
        @JsonProperty("REC_DATE")
        public String recDate;
        @JsonProperty("LOCKING_FLAG")
        public String lockingFlag;
        @JsonProperty("POINT_CODE")
        public String pointCode;
        @JsonProperty("EXP_DATE")
        public String expDate;
        @JsonProperty("CTIME")
        public String cTime;
        @JsonProperty("TRAN_TYPE")
        public String tranType;
        @JsonProperty("INTERNAL_NO")
        public String internalNo;
        @JsonProperty("CARD")
        public String card;
        @JsonProperty("APR_CODE")
        public String aprCode;
        @JsonProperty("MERCHANT")
        public String merchant;
        @JsonProperty("TRAN_AMT")
        public String tranAmt;
        @JsonProperty("MCC_CODE")
        public String mccCode;
        @JsonProperty("COUNTRY")
        public String country;
        @JsonProperty("GROUPC")
        public String groupC;
        @JsonProperty("CARD_ACCT")
        public String cardAcct;
        @JsonProperty("ADD_INFO")
        public String addInfo;
        @JsonProperty("ACCNT_CCY")
        public String accntCcy;
        @JsonProperty("COUNTERPARTY")
        public String counterParty;
        @JsonProperty("BANK_C")
        public String bankC;
        @JsonProperty("AMOUNT_NET")
        public String amountNet;
        @JsonProperty("DEAL_DESC")
        public String dealDesc;
        @JsonProperty("POST_DATE")
        public String postDate;
        @JsonProperty("TR_CODE")
        public String trCode;
        @JsonProperty("CCY_EXP")
        public String ccyExp;
        @JsonProperty("TRAN_CCY")
        public String tranCcy;
        @JsonProperty("TR_FEE")
        public String trFee;
        @JsonProperty("TRAN_DATE_TIME")
        public String tranDateTime;
        @JsonProperty("STAN")
        public String stan;
        @JsonProperty("CLIENT")
        public String client;
        @JsonProperty("APR_SCR")
        public String aprScr;
        @JsonProperty("REF_NUMBER")
        public String refNumber;
        @JsonProperty("CITY")
        public String city;
        @JsonProperty("CL_ACCT_KEY")
        public String clAcctKey;
        @JsonProperty("TR_CODE2")
        public String trCode2;
        @JsonProperty("TR_FEE2")
        public String trFee2;
        @JsonProperty("FLD_104")
        public String fld104;
        @JsonProperty("ACCOUNT_NO")
        public String accountNo;
        @JsonProperty("ACQREF_NR")
        public String acqrefNr;
        @JsonProperty("TERM_ID")
        public String termId;
        @JsonProperty("ABVR_NAME")
        public String abvrName;
    }


}
