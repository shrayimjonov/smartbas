package uz.davrbank.userinfobase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserInfoBaseApplication {


    public static void main(String[] args) {
        SpringApplication.run(UserInfoBaseApplication.class, args);
    }

}
