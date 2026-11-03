package uz.davrbank.journaltransservice.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalDataDTO implements Serializable {

    @JsonProperty("TRANSACTION_ID")
    private String transactionId;

    @JsonProperty("BANK_CL")
    private String bankCL;

    @JsonProperty("ACC_CL")
    private String accCL;

    @JsonProperty("BANK_CO")
    private String bankCO;

    @JsonProperty("ACC_CO")
    private String accCO;

    @JsonProperty("DEBET")
    private Long debet;

    @JsonProperty("CREDIT")
    private Float credit;

    @JsonProperty("PURPOSE")
    private String purpose;

    @JsonProperty("MODULE")
    private String module;

    @JsonProperty("CREATED_EMP")
    private String createdEmp;

    @JsonProperty("CREATED_DATE")
    private String createdDate;

    @JsonProperty("CREATED_TIME")
    private String createdTime;

    @JsonProperty("APPROVED_EMP")
    private String approvedEmp;

    @JsonProperty("APPROVED_DATE")
    private String approvedDate;

    @JsonProperty("APPROVED_TIME")
    private String approvedTime;
}
