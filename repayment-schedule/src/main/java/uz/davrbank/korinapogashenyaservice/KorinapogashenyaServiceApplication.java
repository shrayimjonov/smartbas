package uz.davrbank.korinapogashenyaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static uz.davrbank.korinapogashenyaservice.utils.StartProject.loggingApplicationParams;

@SpringBootApplication
public class KorinapogashenyaServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(KorinapogashenyaServiceApplication.class, args);
        loggingApplicationParams(context.getEnvironment());
    }
}