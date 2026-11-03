package uz.davrbank.bustreasury;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "uz.davrbank.bustreasury.*")
@RefreshScope
public class BusTreasuryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusTreasuryApplication.class, args);
    }

}
