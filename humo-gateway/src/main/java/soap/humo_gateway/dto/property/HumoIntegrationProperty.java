package soap.humo_gateway.dto.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "humo-integration", ignoreUnknownFields = false, ignoreInvalidFields = false)
public class HumoIntegrationProperty {
    private String url;
    private Integer connectTimeOut;
    private Integer readTimeOut;
}
