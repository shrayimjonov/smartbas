package uz.davrbank.remittanceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static uz.davrbank.remittanceservice.utils.StartProject.loggingApplicationParams;

@SpringBootApplication
public class RemittanceServiceApplication {

    public static void main(String[] args) {
      var projectStart =  SpringApplication.run(RemittanceServiceApplication.class, args);
        loggingApplicationParams(projectStart.getEnvironment());
    }
}