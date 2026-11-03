package uz.davrbank.clientaccounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static uz.davrbank.clientaccounts.utils.StartProject.loggingApplicationParams;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableFeignClients(basePackages = "uz.davrbank.clientaccounts")
public class ClientAccountsApplication {
    public static void main(String[] args) {
        var env = SpringApplication.run(ClientAccountsApplication.class, args);
        loggingApplicationParams(env.getEnvironment());
    }
}