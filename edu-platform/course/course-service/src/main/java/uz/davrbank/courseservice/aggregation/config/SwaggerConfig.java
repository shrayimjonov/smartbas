package uz.davrbank.courseservice.aggregation.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Course Service API",
                version = "1.0.0",
                description = "REST API для управления курсами и учебными планами",
                contact = @Contact(
                        name = "Backend Team, Davrbank",
                        email = "user@example.com",
                        url = "https://internal.example.com"
                )
        ),
        servers = {
                @Server(url = "/course-service", description = "Через API Gateway")
        }
)
@Configuration
public class SwaggerConfig {
    // Конфигурация по аннотациям — ничего вручную не нужно
}
