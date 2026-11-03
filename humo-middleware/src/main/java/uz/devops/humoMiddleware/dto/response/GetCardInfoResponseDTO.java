package uz.devops.humoMiddleware.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.basic.RecordInfo;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCardInfoResponseDTO {
    private Integer count;
    private Integer success;
    private Integer fail;
    private List<RecordInfo> records;
}
