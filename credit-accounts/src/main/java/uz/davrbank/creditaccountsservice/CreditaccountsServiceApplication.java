package uz.davrbank.creditaccountsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static uz.davrbank.creditaccountsservice.utils.StartPoint.loggingApplicationParams;

@SpringBootApplication
@EnableFeignClients(basePackages = "uz.davrbank.creditaccountsservice")
public class CreditaccountsServiceApplication {

    public static void main(String[] args) {

        var en = SpringApplication.run(CreditaccountsServiceApplication.class, args);
        loggingApplicationParams(en.getEnvironment());
    }

}
