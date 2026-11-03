package uz.davrbank.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.policy.SimpleRetryPolicy;

@Configuration
@EnableRetry
public class RetryConfig {

    @Bean
    public RetryPolicy customRetryPolicy() {
        return new SimpleRetryPolicy(5); // Retry up to 5 times
    }
}
