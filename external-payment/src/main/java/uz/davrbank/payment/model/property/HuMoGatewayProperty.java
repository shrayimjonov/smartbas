package uz.davrbank.payment.model.property;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.humo-gateway")
@EqualsAndHashCode(callSuper = true)
public class HuMoGatewayProperty extends BaseProperty {
    private String username;
    private String password;
}
