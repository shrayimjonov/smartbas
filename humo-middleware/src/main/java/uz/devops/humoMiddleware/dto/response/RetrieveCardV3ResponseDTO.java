package uz.devops.humoMiddleware.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.basic.AccountRecord;
import uz.devops.humoMiddleware.dto.basic.BalanceRecord;
import uz.devops.humoMiddleware.dto.basic.CardRecord;
import uz.devops.humoMiddleware.dto.basic.MobileBank;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveCardV3ResponseDTO {

    @NotNull
    @ApiModelProperty("Count of retrieved Account records")
    private Long listSize;

    @NotNull
    @ApiModelProperty("Card records")
    private CardRecord card;

    @ApiModelProperty("List of Account records")
    private List<AccountRecord> account;

    @NotNull
    @ApiModelProperty("Balance records")
    private BalanceRecord balance;

    @NotNull
    @ApiModelProperty("Mobile Bank status")
    private MobileBank mb;
}
