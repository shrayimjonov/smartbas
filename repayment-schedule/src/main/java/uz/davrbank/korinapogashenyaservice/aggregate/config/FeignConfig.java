package uz.davrbank.korinapogashenyaservice.aggregate.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 22.01.2025
 * Time: 17:30
 * Project: korinapogasheniya
 */
@Configuration
@EnableFeignClients(basePackages = "uz.davrbank.korinapogashenyaservice.aggregate.integration")
public class FeignConfig {
}
