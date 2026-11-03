package humo.integration.card.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HumoVirtualReq {
    private String branch;
    private String clientId;
    private String cardType;
    private String user;
    private String phone;
    private String account;
}
