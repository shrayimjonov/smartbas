package uz.devops.humoMiddleware.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.enums.AccountType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


//  Added for documentation v2.4.0

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "запрос получение базовой карты")
public class RetrieveCardV3RequestDTO {

    @ApiModelProperty("PAN, card number")
    @NotNull
    @Size(max = 19)
    private String primaryAccountNumber;

    @ApiModelProperty("Account type for filtering accounts by account type")
    private AccountType accountType;

    @ApiModelProperty("Currency for filtering accounts by currency (for example, EUR, RUR, USD)")
    @Size(max = 3)
    private String currency;

    @ApiModelProperty("Number flag. If is set to ‘1’ then the exponents for accounts' currencies and balance currency will be returned. The default value is ‘0’.")
    private String returnExponent;

    @ApiModelProperty("Use this field to get mobile banking status 0 or 1")
    @JsonProperty(value = "mb_flag")
    private Integer flag;
}
