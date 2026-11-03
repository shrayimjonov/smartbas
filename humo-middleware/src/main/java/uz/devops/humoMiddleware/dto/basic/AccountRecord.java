package uz.devops.humoMiddleware.dto.basic;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.enums.AccountStatus;
import uz.devops.humoMiddleware.dto.enums.AccountType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Account Record")
public class AccountRecord {

    @ApiModelProperty("CENTRE_ID")
    @NotNull
    @Size(max = 11)
    private String institutionId;

    @ApiModelProperty("ACCOUNT_ID")
    @NotNull
    @Size(max = 28)
    private String accountId;

    @ApiModelProperty("ACCOUNT_ID_BANK")
    @NotNull
    @Size(max = 35)
    private String bankAccountId;

    @ApiModelProperty("ACCOUNT_CCY")
    private String currency;

    @ApiModelProperty("CRD_HOLDR_ID")
    @NotNull
    @Size(max = 19)
    private String cardholderId;

    @ApiModelProperty("EFFECTIVE_DATE")
    private String effectiveDate;

    @ApiModelProperty("UPDATE_DATE")
    private String updateDate;

    @ApiModelProperty("ACCOUNT_TYPE")
    @NotNull
    private AccountType accountType;

    @ApiModelProperty("AMOUNT_SET_TIME")
    private String amountSetTime;

    @ApiModelProperty("INITIAL_AMOUNT")
    @Size(max = 16)
    private Long initialAmount;

    @ApiModelProperty("BONUS_AMOUNT")
    @Size(max = 16)
    private Long bonusAmount;

    @ApiModelProperty("BONUS_AMOUNT_EXPIRY")
    @Size(max = 16)
    private String bonusAmountExpiry;

    @ApiModelProperty("CREDIT_LIMIT")
    @Size(max = 16)
    private String creditLimit;

    @ApiModelProperty("CREDIT_LIMIT_EXPIRY")
    private String creditLimitExpiry;

    @ApiModelProperty("LOCK_AMOUNT_CMS")
    @Size(max = 16)
    private Long lockedBackofficeAmount;

    @ApiModelProperty("LOCK_TIME")
    @NotNull
    private String lockTime;

    @ApiModelProperty("Lock sum from STIP_LOCKS")
    @NotNull
    private Long lockedAmount;

    @ApiModelProperty("SHADOW_AMOUNT")
    @NotNull
    @Size(max = 16)
    private Long shadowAmount;

    @ApiModelProperty("COMM_GRP")
    @Size(max = 12)
    private String commissionGroup;

    @ApiModelProperty("TIME_STAMP")
    @Size(max = 31)
    private String timestamp;

    @ApiModelProperty("PRIORITY")
    @NotNull
    private Long priority;

    @ApiModelProperty("STATUS")
    @NotNull
    private AccountStatus status;

    @ApiModelProperty("ADD_INFO")
    @Size(max = 999)
    private String additionalInfo;

    @ApiModelProperty("Available amount calculated from initial amount, credit limit, bonus amount and locked amounts.")
    @NotNull
    private Long availableAmount;

    @ApiModelProperty("Optional field. Contains account’s currency exponent value.")
    private Integer exponent;

}
