package uz.devops.humoMiddleware.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "humo-middleware")
public class HumoMiddlewareProperties {
    private String name = "humo-middleware-sample2";
    private String token;
    private String simulate;
    private String url;
    private Boolean isTestHumo;
}
