package uz.davrbank.auditaccountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static uz.davrbank.auditaccountservice.utils.AccountStartPoint.loggingApplicationParams;

@SpringBootApplication
public class AuditaccountServiceApplication {

    public static void main(String[] args) {
      var envir =  SpringApplication.run(AuditaccountServiceApplication.class, args);
        loggingApplicationParams(envir.getEnvironment());
    }
}