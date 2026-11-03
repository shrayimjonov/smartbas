package uz.davrbank.payment.model.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "external.billing")
@Configuration
public class BillingProperty {
    private String url;
    private Integer readTimeOut;
    private Integer connectTimeOut;
    private Keycloak keycloak;
    private Map<String, String> services;


    @Getter
    @Setter
    @ToString
    public static class Keycloak {
        private String url;
        private String username;
        private String password;
        private String clientId;
        private String clientSecret;
        private String grantType;
        private String scope;
    }


}
