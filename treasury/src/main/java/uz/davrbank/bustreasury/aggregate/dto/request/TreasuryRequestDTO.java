    package uz.davrbank.bustreasury.aggregate.dto.request;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.math.BigDecimal;
    import java.util.Date;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class TreasuryRequestDTO {
        @JsonProperty("P_ACC_CL")
        private String debitAccount;

        @JsonProperty("P_ACC_CO")
        private String creditAccount;

        @JsonProperty("P_NAME_CO")
        private String companyName;

        @JsonProperty("P_BANK_CO")
        private String companyBank;

        @JsonProperty("P_INN_CO")
        private String companyInn;

        @JsonProperty("P_SUMMA")
        private String sum;

        @JsonProperty("P_DOC_NUM")
        private String documentNumber;

        @JsonProperty("P_DATE")
        @JsonFormat(pattern = "dd.MM.yyyy")
        private Date date;

        @JsonProperty("CODE_PLAT")
        private String paymentCode;

        @JsonProperty("P_PURPOSE_ID")
        private String purposeId;

        @JsonProperty("P_PURPOSE")
        private String purpose;

        @JsonProperty("P_TYPE_DOC")
        private String documentType;

        @JsonProperty("P_TYPE_PLAT")
        private Long paymentType;

        @JsonProperty("P_STATE")
        private Integer state;

        @JsonProperty("P_BRANCH")
        private String branch;

        @JsonProperty("TYPE_PLAT")
        private String typePlate;
    }
