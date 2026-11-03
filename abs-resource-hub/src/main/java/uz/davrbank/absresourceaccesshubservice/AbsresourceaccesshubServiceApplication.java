package uz.davrbank.absresourceaccesshubservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static uz.davrbank.absresourceaccesshubservice.util.ProjectStart.loggingApplicationParams;

@SpringBootApplication
public class AbsresourceaccesshubServiceApplication {

    public static void main(String[] args) {
      var project =  SpringApplication.run(AbsresourceaccesshubServiceApplication.class, args);
        loggingApplicationParams(project.getEnvironment());
    }

}
