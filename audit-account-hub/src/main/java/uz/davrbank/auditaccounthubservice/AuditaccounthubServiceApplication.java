package uz.davrbank.auditaccounthubservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static uz.davrbank.auditaccounthubservice.utils.StartPoint.loggingApplicationParams;

@SpringBootApplication
public class AuditaccounthubServiceApplication {

    public static void main(String[] args) {
        var env =  SpringApplication.run(AuditaccounthubServiceApplication.class, args);
        loggingApplicationParams(env.getEnvironment());
    }

}
