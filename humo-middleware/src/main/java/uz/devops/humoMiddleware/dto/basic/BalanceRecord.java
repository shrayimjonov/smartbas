package uz.devops.humoMiddleware.dto.basic;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Balance Record")
public class BalanceRecord {

    @ApiModelProperty("ACCOUNT_CCY")
    @NotNull
    private String currency;

    @ApiModelProperty("INITIAL_AMOUNT")
    @NotNull
    @Size(max = 16)
    private Long initialAmount;

    @ApiModelProperty("BONUS_AMOUNT")
    @NotNull
    @Size(max = 16)
    private Long bonusAmount;

    @ApiModelProperty("CREDIT_LIMIT")
    @NotNull
    @Size(max = 16)
    private Long creditLimit;

    @ApiModelProperty("LOCK_AMOUNT_CMS")
    @NotNull
    @Size(max = 16)
    private Long lockedBackofficeAmount;

    @ApiModelProperty("LOCK_AMOUNT_CMS_OFL")
    @NotNull
    @Size(max = 16)
    private Long lockedBackofficeAmountOffline;

    @ApiModelProperty("Lock sum from STIP_LOCKS")
    @NotNull
    private Long lockedAmount;

    @ApiModelProperty("Available amount calculated from initial amount, credit limit, bonus amount and locked amounts")
    @NotNull
    private Long availableAmount;

    @ApiModelProperty("Optional field. Contains account’s currency exponent value")
    private Long exponent;
}
