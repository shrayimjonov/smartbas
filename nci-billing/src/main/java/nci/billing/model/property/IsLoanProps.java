package nci.billing.model.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "nci.credential", ignoreUnknownFields = false)
public class IsLoanProps {
    private Credential genesis;
    private Credential paymo;

    @Getter
    @Setter
    public static class Credential {
        private String username;
        private String password;
    }
}
