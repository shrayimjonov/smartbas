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
@ApiModel(value = "change")
public class Charge {
    @Size(max = 34)
    @ApiModelProperty("Charge product ID that holds charge amount,currency and further processing conditions.")
    private String agreementCharge;

    @Size(max = 255)
    @ApiModelProperty("CMS charge account number(account_no/tranz_acct)")
    private String chargeAccount;

    @Size(max = 8)
    @ApiModelProperty("Notification related data")
    private String chargeDate;

    public Charge(String agreementCharge, String chargeAccount) {
        this.agreementCharge = agreementCharge;
        this.chargeAccount = chargeAccount;
    }
}
