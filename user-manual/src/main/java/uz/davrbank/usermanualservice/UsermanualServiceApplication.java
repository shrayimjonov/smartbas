package uz.davrbank.usermanualservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import uz.davrbank.usermanualservice.config.ApplicationProperties;

import static uz.davrbank.usermanualservice.utils.StartProject.loggingApplicationParams;

@SpringBootApplication
@EnableConfigurationProperties(value = ApplicationProperties.class)
public class UsermanualServiceApplication {

    public static void main(String[] args) {
        var usermanual = SpringApplication.run(UsermanualServiceApplication.class, args);
        loggingApplicationParams(usermanual.getEnvironment());
    }
}
