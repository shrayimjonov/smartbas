package uz.davrbank.journaltransservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import static uz.davrbank.journaltransservice.utils.StartingProject.loggingApplicationParams;

@SpringBootApplication
@EnableFeignClients
public class JournaltransServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext projectStart;
        projectStart = SpringApplication.run(JournaltransServiceApplication.class, args);
        loggingApplicationParams(projectStart.getEnvironment());
    }

}
