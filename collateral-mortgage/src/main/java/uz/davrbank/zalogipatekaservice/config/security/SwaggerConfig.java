package uz.davrbank.zalogipatekaservice.config.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@OpenAPIDefinition
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Value("${openapi.service.title}")
    private String title;
    @Value("${openapi.service.description}")
    private String description;
    @Value("${openapi.service.version}")
    private String version;
    @Value("${openapi.service.urls}")
    private String urls;
    private final Environment environment;

    public SwaggerConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> serverList = new ArrayList<>();
        String contextPath = environment.getProperty("server.servlet.context-path");
        if (urls != null && !urls.isBlank()) {
            Arrays.stream(urls.split("\\r?\\n"))
                    .forEach(url -> serverList
                            .add(new Server()
                                    .url(url + contextPath)));
        }
            return new OpenAPI()
                    .servers(serverList)
                    .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                    .components(new io.swagger.v3.oas.models.Components()
                            .addSecuritySchemes("bearerAuth",
                                    new io.swagger.v3.oas.models.security.SecurityScheme()
                                            .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")
                            )
                    )
                    .info(new Info()
                            .title(title)
                            .description(description)
                            .version(version));
        }
    }
