package uz.devops.humoMiddleware.dto.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.devops.humoMiddleware.dto.request.ClientInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerActivate {
    private String id;
    private ClientInfo params;
}
