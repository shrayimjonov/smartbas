package uz.davrbank.yeisvoservice.aggregate.prop;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


import java.util.Map;


@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "external")
public class ExternalProp {

    private Map<String,ExternalConfig> configs;

    public ExternalConfig getConfig(String key){
        return configs.get(key);
    }


    @Getter @Setter
    public static class ExternalConfig {
        private String service;
        private String balanced;
        private String host;
        private String port;
        private String port1;
        private String username;
        private String password;
        private String protocol;
        private String baseUrl;
        private String authBaseUrl;
        private String authUsername;
        private String authPassword;
        private String authBasic;
        private String authGrantType;

    }
}