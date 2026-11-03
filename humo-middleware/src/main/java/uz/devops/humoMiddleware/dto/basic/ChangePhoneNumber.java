package uz.devops.humoMiddleware.dto.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.request.ChangeSmsInfoNumberRequestDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePhoneNumber {
    private String id;
    private ChangeSmsInfoNumberRequestDTO params;
}
