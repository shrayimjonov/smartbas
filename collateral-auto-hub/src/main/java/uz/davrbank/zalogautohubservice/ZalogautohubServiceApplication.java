package uz.davrbank.zalogautohubservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static uz.davrbank.zalogautohubservice.utils.StartingPoint.loggingApplicationParams;

@SpringBootApplication
public class ZalogautohubServiceApplication {

    public static void main(String[] args) {
        var env = SpringApplication.run(ZalogautohubServiceApplication.class, args);
        loggingApplicationParams(env.getEnvironment());
    }
}