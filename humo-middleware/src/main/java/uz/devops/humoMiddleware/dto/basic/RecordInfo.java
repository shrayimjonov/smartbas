package uz.devops.humoMiddleware.dto.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordInfo {
    private String status;
    private Integer index;
    private List<CardDataInfo> data;
}
