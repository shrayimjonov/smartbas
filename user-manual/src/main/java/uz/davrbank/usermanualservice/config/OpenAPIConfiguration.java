package uz.davrbank.usermanualservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class OpenAPIConfiguration {

    Environment environment;

    @Bean
    public OpenAPI openAPI() {
        String url = "http://" + getHostAddress() + ":" + environment.getProperty("server.port");

        return new OpenAPI()
                .info(new Info()
                        .title("Usermanual API documentation"))
                .servers(List.of(new Server().url(url)))
                .components(new Components()
                        .addSecuritySchemes("access_token", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer"))
                )
                .security(Collections.singletonList(new SecurityRequirement().addList("access_token")));
    }

    private static String getHostAddress() {
        String hostAddress;

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "localhost";
            log.error("Can't determine host address");
        }
        return hostAddress;
    }
}