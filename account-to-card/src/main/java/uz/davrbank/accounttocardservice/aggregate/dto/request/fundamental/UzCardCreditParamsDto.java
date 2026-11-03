package uz.davrbank.accounttocardservice.aggregate.dto.request.fundamental;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UzCardCreditParamsDto {
    private UzCardRequestDataDto tran;
}
