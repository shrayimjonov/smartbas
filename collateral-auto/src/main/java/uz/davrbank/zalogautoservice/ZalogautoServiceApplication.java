package uz.davrbank.zalogautoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static uz.davrbank.zalogautoservice.utils.ZalogStartPoint.loggingApplicationParams;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableFeignClients(basePackages = "uz.davrbank.zalogautoservice")
public class ZalogautoServiceApplication {
    public static void main(String[] args) {
        var env = SpringApplication.run(ZalogautoServiceApplication.class, args);
        loggingApplicationParams(env.getEnvironment());
    }
}