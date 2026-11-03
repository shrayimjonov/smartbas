package uz.davrbank.accounttocardservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static uz.davrbank.accounttocardservice.utils.StartProject.loggingApplicationParams;

@SpringBootApplication
public class AccounttocardServiceApplication {

    public static void main(String[] args) {
        var acc = SpringApplication.run(AccounttocardServiceApplication.class, args);
        loggingApplicationParams(acc.getEnvironment());
    }
}