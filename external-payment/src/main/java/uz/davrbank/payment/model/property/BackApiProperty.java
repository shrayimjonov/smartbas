package uz.davrbank.payment.model.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "external.back-api")
public class BackApiProperty extends BaseProperty {
    private String username;
    private String password;
    private String parentId;
}
