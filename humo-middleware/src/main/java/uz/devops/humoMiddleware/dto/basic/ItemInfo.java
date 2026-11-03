package uz.devops.humoMiddleware.dto.basic;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfo {
    private String type;
    private String actionCode;
    private String actionDescription;
    private String effectiveDate;
}
