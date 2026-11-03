package uz.davrbank.bustreasury.aggregate.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckStatusResponseDTO {

    @JsonProperty("ERROR_CODE")
    private String ERROR_CODE;
    @JsonProperty("ERROR_NOTE")
    private String ERROR_NOTE;
    @JsonProperty("TRANS_ID")
    private String TRANS_ID;
    @JsonProperty("STATE_NAME")
    private String stateName;
//    @JsonProperty("ID")
//    private Long id;
//    @JsonProperty("BRANCH")
//    private String branch;
//    @JsonProperty("DOC_NUM")
//    private String docNum;
//    @JsonProperty("D_DATE")
//    private String dDate;
//    @JsonProperty("BANK_CL")
//    private String bankCl;
//    @JsonProperty("ACC_CL")
//    private String accCl;
//    @JsonProperty("NAME_CL")
//    private String nameCl;
//    @JsonProperty("BANK_CO")
//    private String bankCo;
//    @JsonProperty("ACC_CO")
//    private String accCo;
//    @JsonProperty("NAME_CO")
//    private String nameCo;
//    @JsonProperty("PURPOSE")
//    private String purpose;
//    @JsonProperty("SUMMA")
//    private Long summa;
//    @JsonProperty("CURRENCY")
//    private String currency;
//    @JsonProperty("TYPE_DOC")
//    private String typeDoc;
//    @JsonProperty("S_DEAL_ID")
//    private Long sDealId;
//    @JsonProperty("V_DATE")
//    private String vDate;
//    @JsonProperty("PDC")
//    private String pdc;
//    @JsonProperty("CASH_CODE")
//    private String cashCode;
//    @JsonProperty("STATE")
//    private Long state;
//    @JsonProperty("PARENT_GROUP_ID")
//    private Long parentGroupId;
//    @JsonProperty("PARENT_ID")
//    private Long parentId;
//    @JsonProperty("CHILD_ID")
//    private Long childId;
//    @JsonProperty("KOD_ERROR")
//    private Long kodError;
//    @JsonProperty("FILE_NAME")
//    private String fileName;
//    @JsonProperty("EGG_GENERAL")
//    private String eggGeneral;
//    @JsonProperty("EMP_ID")
//    private Long empId;
//    @JsonProperty("ID_TRANSH")
//    private Long idTransh;
//    @JsonProperty("ID_TRANSH_PURP")
//    private Long idTranshPurp;
//    @JsonProperty("VAL_DATE")
//    private String valDate;
//    @JsonProperty("BATCH_TYPE_ID")
//    private Long batchTypeId;

    }



