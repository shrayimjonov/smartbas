package uz.davrbank.schotoborotservice.aggregate.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 15.12.2025
 * Time: 13:29
 * Project: schotoborot-service
 */
@Configuration
@EnableFeignClients(basePackages = "uz.davrbank.schotoborotservice.aggregate.integration")
public class FeignConfig {
}
