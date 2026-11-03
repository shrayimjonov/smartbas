package nci.billing.model.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "nci.loan-api", ignoreUnknownFields = false)
public class LoanProps {
    private String url;
    private String username;
    private String password;
    private String realToken;
    private String testToken;
}
