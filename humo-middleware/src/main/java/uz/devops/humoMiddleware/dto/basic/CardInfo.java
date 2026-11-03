package uz.devops.humoMiddleware.dto.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardInfo {
    private String pan;
    private String expiry;
    private String nameOnCard;
    private String cardholderId;
    private StatusInfo statuses;
    private String pinTryCount;
}
