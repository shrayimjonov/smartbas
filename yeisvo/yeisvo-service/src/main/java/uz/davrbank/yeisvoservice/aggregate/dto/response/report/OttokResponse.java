package uz.davrbank.yeisvoservice.aggregate.dto.response.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OttokResponse implements Serializable{

    @JsonProperty("DATE_OPER")
    private String DATE_OPER;

    @JsonProperty("INN_CLIENT")
    private String INN_CLIENT;

    @JsonProperty("NAME_CLIENT")
    private String NAME_CLIENT;

    @JsonProperty("IDN")
    private String IDN;

    @JsonProperty("OKED")
    private String OKED;

    @JsonProperty("OKONX")
    private String OKONX;

    @JsonProperty("KOD_REGION")
    private String KOD_REGION;

    @JsonProperty("KOD_STRANA")
    private String KOD_STRANA;

    @JsonProperty("KOD_OFFSHORE1")
    private String KOD_OFFSHORE1;

    @JsonProperty("KOD_OFFSHORE2")
    private String KOD_OFFSHORE2;

    @JsonProperty("KOD_VAL")
    private String KOD_VAL;

    @JsonProperty("SUMMA_7")
    private Long SUMMA_7;

    @JsonProperty("SUMMA_8")
    private Long SUMMA_8;

    @JsonProperty("SUMMA_811")
    private Long SUMMA_811;

    @JsonProperty("SUMMA_812")
    private Long SUMMA_812;

    @JsonProperty("SUMMA_821")
    private Long SUMMA_821;

    @JsonProperty("SUMMA_822")
    private Long SUMMA_822;

    @JsonProperty("SUMMA_823")
    private Long SUMMA_823;

    @JsonProperty("SUMMA_9")
    private Long SUMMA_9;

    @JsonProperty("SUMMA_10")
    private Long SUMMA_10;

    @JsonProperty("SUMMA_11")
    private Long SUMMA_11;

    @JsonProperty("SUMMA_12")
    private Long SUMMA_12;

    @JsonProperty("SUMMA_13")
    private Long SUMMA_13;

    @JsonProperty("SUMMA_14")
    private Long SUMMA_14;

    @JsonProperty("SUMMA_15")
    private Long SUMMA_15;

    @JsonProperty("SUMMA_16")
    private Long SUMMA_16;

    @JsonProperty("SUMMA_17")
    private Long SUMMA_17;

    @JsonProperty("SUMMA_18")
    private Long SUMMA_18;

    @JsonProperty("SUMMA_19")
    private Long SUMMA_19;

    @JsonProperty("SUMMA_20")
    private Long SUMMA_20;

    @JsonProperty("SUMMA_21")
    private Long SUMMA_21;

    @JsonProperty("VID_OPER")
    private String VID_OPER;

    @JsonProperty("SUMMA_CHECK_1")
    private Long SUMMA_CHECK_1;

    @JsonProperty("SUMMA_CHECK_2")
    private Long SUMMA_CHECK_2;

    @JsonProperty("GENERAL_ID")
    private Long GENERAL_ID;

    @JsonProperty("ACC_CL")
    private String ACC_CL;

    @JsonProperty("ACC_CO")
    private String ACC_CO;

    @JsonProperty("TRANS_KOD1")
    private String TRANS_KOD1;

    @JsonProperty("PURPOSE")
    private String PURPOSE;
}
