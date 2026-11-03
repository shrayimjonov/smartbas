package uz.davrbank.esudservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static uz.davrbank.esudservice.utils.StartPoint.loggingApplicationParams;

@SpringBootApplication
@EnableFeignClients(basePackages = "uz.davrbank.esudservice.aggregate.feign")
public class EsudServiceApplication {

    public static void main(String[] args) {
        var projectPoint = SpringApplication.run(EsudServiceApplication.class, args);
        loggingApplicationParams(projectPoint.getEnvironment());
    }
}