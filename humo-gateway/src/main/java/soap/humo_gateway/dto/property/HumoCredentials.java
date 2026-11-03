package soap.humo_gateway.dto.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "humo")
@Getter
@Setter
public class HumoCredentials {
    private String infoUrl;
    private Long infoTimeout;
    private String paymentUrl;
    private String pinUrl;
    private Long paymentTimeout;
    private Long actionTimeout;
    private String login;
    private String password;
    private String panRegex;
    private String pointCode;
    private String posTerminal;
    private String ZPK;
    private String balanceUrl;
    private Long balanceTimeout;
    private String limitUrl;
    private Long limitTimeout;

}
