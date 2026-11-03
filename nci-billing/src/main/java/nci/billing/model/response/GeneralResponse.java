package nci.billing.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralResponse {
    private String status;
    private Result result;
    private Error error;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {
        public Long code;
        public String message;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private String id;
        private String state;
        private String branch;
        private String currency;
        private String kod_ERR;
        private Object file_NAME;
        private String doc_NUM;
        private String d_DATE;
        private String bank_CL;
        private String acc_CL;
        private String name_CL;
        private String bank_CO;
        private String acc_CO;
        private String name_CO;
        private String purpose;
        private String summa;
        private String type_DOC;
        private String s_DEAL_ID;
        private String v_DATE;
        private String pdc;
        private Object cash_CODE;
        private String parent_GROUP_ID;
        private String parent_ID;
        private Object child_ID;
        private Object err_GENERAL;
        private String emp_ID;
        private Object id_TRANSH;
        private String id_TRANSH_PURP;
        private Object val_DATE;
        private String batch_TYPE_ID;
    }

}
