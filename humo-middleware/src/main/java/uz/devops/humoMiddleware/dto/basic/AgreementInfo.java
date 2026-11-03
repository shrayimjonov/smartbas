package uz.devops.humoMiddleware.dto.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementInfo {
    private String description;
    private String state;
    private String phone;
}
