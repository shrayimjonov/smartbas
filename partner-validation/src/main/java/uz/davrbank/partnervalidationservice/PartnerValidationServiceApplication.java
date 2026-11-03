package uz.davrbank.partnervalidationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import uz.davrbank.partnervalidationservice.config.ApplicationProperties;

import static uz.davrbank.partnervalidationservice.util.StartProject.loggingApplicationParams;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class PartnerValidationServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(PartnerValidationServiceApplication.class, args);
        loggingApplicationParams(applicationContext.getEnvironment());
    }
}