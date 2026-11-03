package uz.devops.humoMiddleware.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMaskedPanRequestDTO {
    private String pan;
}
