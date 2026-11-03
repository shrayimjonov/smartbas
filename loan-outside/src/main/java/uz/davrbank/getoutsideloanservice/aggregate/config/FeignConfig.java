package uz.davrbank.getoutsideloanservice.aggregate.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "uz.davrbank.getoutsideloanservice.aggregate.integration")
public class FeignConfig {
}
