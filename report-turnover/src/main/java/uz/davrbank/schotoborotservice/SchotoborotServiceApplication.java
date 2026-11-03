package uz.davrbank.schotoborotservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static uz.davrbank.schotoborotservice.utils.StartProject.loggingApplicationParams;

@SpringBootApplication
public class SchotoborotServiceApplication {

    public static void main(String[] args) {
        var pro = SpringApplication.run(SchotoborotServiceApplication.class, args);
        loggingApplicationParams(pro.getEnvironment());
    }
}