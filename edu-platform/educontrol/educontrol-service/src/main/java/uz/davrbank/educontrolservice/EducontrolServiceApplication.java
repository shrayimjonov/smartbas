package uz.davrbank.educontrolservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@RefreshScope
@ComponentScan("uz.davrbank.educontrolservice.*")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class EducontrolServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EducontrolServiceApplication.class, args);
    }

}
