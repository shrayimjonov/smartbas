package uz.davrbank.getoutsideloanservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static uz.davrbank.getoutsideloanservice.utils.StartProject.loggingApplicationParams;

@SpringBootApplication
public class GetoutsideloanServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GetoutsideloanServiceApplication.class, args);
        loggingApplicationParams(context.getEnvironment());
    }

}
