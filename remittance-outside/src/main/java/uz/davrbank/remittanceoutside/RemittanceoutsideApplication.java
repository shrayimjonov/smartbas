package uz.davrbank.remittanceoutside;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import uz.davrbank.remittanceoutside.aggregate.configuration.ApplicationProperties;

import static uz.davrbank.remittanceoutside.utils.StartProject.loggingApplicationParams;

@SpringBootApplication
@EnableConfigurationProperties(value = ApplicationProperties.class)
public class RemittanceoutsideApplication {

    public static void main(String[] args) {
      var here =  SpringApplication.run(RemittanceoutsideApplication.class, args);
        loggingApplicationParams(here.getEnvironment());
    }

}
