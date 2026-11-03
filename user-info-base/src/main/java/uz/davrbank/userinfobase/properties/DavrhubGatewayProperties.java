package uz.davrbank.userinfobase.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Data
@ConfigurationProperties(prefix = "loan.davrhub.gateway")
public class DavrhubGatewayProperties {

    private String baseUrl;
    private String clientUsername;
    private String clientPassword;
    private String basicAuthUsername;
    private String basicAuthPassword;
    private String grantType = "password";
    private String serviceName = "userinfobase-service";
    private Duration connectTimeout = Duration.ofSeconds(10);
    private Duration readTimeout = Duration.ofSeconds(120);
    private Duration writeTimeout = Duration.ofSeconds(120);
    private Duration requestTimeout = Duration.ofSeconds(120);
    private Duration tokenExpirySkew = Duration.ofSeconds(60);
}
