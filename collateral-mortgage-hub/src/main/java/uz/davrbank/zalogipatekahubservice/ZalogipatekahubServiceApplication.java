package uz.davrbank.zalogipatekahubservice;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import static uz.davrbank.zalogipatekahubservice.utils.StartingPoint.loggingApplicationParams;

@SpringBootApplication
@EnableAsync
@EnableEncryptableProperties
public class ZalogipatekahubServiceApplication {
    public static void main(String[] args) {
        var jumpEnv = SpringApplication.run(ZalogipatekahubServiceApplication.class, args);
        loggingApplicationParams(jumpEnv.getEnvironment());
    }
}
