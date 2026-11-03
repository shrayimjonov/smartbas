package uz.davrbank.assignmentservice.aggregation.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Assignment Service API",
                version = "1.0.0",
                description = "REST API для работы с заданиями в системе",
                contact = @Contact(
                        name = "Backend Team, Davrbank",
                        email = "user@example.com",
                        url = "https://internal.example.com"
                )
        ),
        servers = {
                @Server(url = "/")
        }
)
@Configuration
public class SwaggerConfig {
    // Конфигурация по аннотациям — ничего вручную не нужно
}
