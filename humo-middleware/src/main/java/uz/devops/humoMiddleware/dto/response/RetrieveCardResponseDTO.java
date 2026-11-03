package uz.devops.humoMiddleware.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.basic.CardRecord;
import uz.devops.humoMiddleware.dto.basic.MobileBank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveCardResponseDTO {
    private CardRecord card;
    private MobileBank mb;
}
