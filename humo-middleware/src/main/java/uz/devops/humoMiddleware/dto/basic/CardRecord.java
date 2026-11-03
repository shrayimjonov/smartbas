package uz.devops.humoMiddleware.dto.basic;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Card Record")
public class CardRecord {
    @ApiModelProperty("CENTRE_ID")
    @NotNull
    @Size(max = 11)
    private String institutionId;

    @ApiModelProperty("CARD_NUMBER")
    @NotNull
    @Size(max = 19)
    private String primaryAccountNumber;

    @ApiModelProperty("EFFECTIVE_DATE")
    private String effectiveDate;

    @ApiModelProperty("UPDATE_DATE")
    private String updateDate;

    @ApiModelProperty("PREF_REC_NUM")
    @Size(max = 11)
    private String prefixNumber;

    @ApiModelProperty("EXPIRY_DATE_1")
    @NotNull
    @Size(max = 4)
    private String expiry;

    @ApiModelProperty("EXPIRY_DATE_2")
    @Size(max = 4)
    private String expiry2;

    @ApiModelProperty("CARD_SEQ_1")
    @NotNull
    @Size(max = 3)
    private String cardSequenceNumber;

    @ApiModelProperty("CARD_SEQ_2")
    @Size(max = 3)
    private String cardSequenceNumber2;

    @ApiModelProperty("CRD_HOLDR_ID")
    @NotNull
    @Size(max = 19)
    private String cardholderId;

    @ApiModelProperty("CRD_HOLDR_NAME")
    @NotNull
    @Size(max = 30)
    private String nameOnCard;

    @ApiModelProperty("CRD_HOLDR_PWD")
    @Size(max = 20)
    private String cardholderPassword;

    @ApiModelProperty("CRD_HOLDR_MSG")
    @Size(max = 99)
    private String cardholderMessage;

    @ApiModelProperty("ACNT_RESTR")
    @Size(max = 20)
    private String accountRestrictionsFlag;

    @ApiModelProperty("COMM_GRP")
    @Size(max = 12)
    private String commissionGroup;

    @ApiModelProperty("CLIENT_ID")
    @NotNull
    @Size(max = 19)
    private String cardUserId;

    @ApiModelProperty("ADD_INFO")
    @Size(max = 99)
    private String additionalInfo;

    @ApiModelProperty("PARAM_GRP_1")
    @Size(max = 5)
    private String riskGroup;

    @ApiModelProperty("PARAM_GRP_2")
    @Size(max = 5)
    private String riskGroup2;

    @ApiModelProperty("BANK_CODE")
    @NotNull
    @Size(max = 2)
    private String bankC;

    @ApiModelProperty("CARD_PIN_TRY_COUNT")
    @NotNull
    private String pinTryCount;

    @ApiModelProperty("Card, Issuer, User and Hot statusinfo")
    @NotNull
    private CardStatus statuses;
}
