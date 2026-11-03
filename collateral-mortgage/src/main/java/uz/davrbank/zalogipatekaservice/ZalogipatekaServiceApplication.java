package uz.davrbank.zalogipatekaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static uz.davrbank.zalogipatekaservice.utils.StartPoint.loggingApplicationParams;

@SpringBootApplication
@EnableFeignClients(basePackages = "uz.davrbank.zalogipatekaservice")
public class ZalogipatekaServiceApplication {

    public static void main(String[] args)
    {
        var en =  SpringApplication.run(ZalogipatekaServiceApplication.class, args);
        loggingApplicationParams(en.getEnvironment());
    }

}
