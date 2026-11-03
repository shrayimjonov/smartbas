package uz.davrbank.bustreasury.aggregate.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Bus Treasury API",
                version = "1.0.0",
                description = "API-документация для системы Bus Treasury — управление валютными операциями, проводками и казначейскими процессами.",
                license = @License(
                        name = "Davrbank Internal Use Only",
                        url = "https://davrbank.uz"
                ),
                termsOfService = "Все права защищены © Davrbank"
        ),
        servers = {
                @Server(
                        description = "Локальная среда (Local ENV)",
                        url = "http://localhost:7001/bus"
                ),
                @Server(
                        description = "Внутренняя сеть (Intranet)",
                        url = "http://127.0.0.1:7001/bus"
                )
        }
)
public class OpenApiSwaggerConfig {
}
