package uz.devops.humoMiddleware.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCardInfoRequestDTO {
    private String card;
    private String expiry;
}
