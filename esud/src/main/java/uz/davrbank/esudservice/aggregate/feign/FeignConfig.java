package uz.davrbank.esudservice.aggregate.feign;

import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import jakarta.validation.Validator;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration class for customizing Feign clients.
 * <p>
 * This configuration defines the following:
 * <ul>
 *     <li>A custom {@link Decoder} that validates request/response DTOs using Bean Validation.</li>
 *     <li>Timeout settings for Feign HTTP requests, including connect and read timeouts, and follow redirects behavior.</li>
 *     <li>Retry policy for failed requests, including number of attempts and interval settings.</li>
 * </ul>
 * </p>
 */
@Configuration
public class FeignConfig {

    /**
     * Provides a {@link Decoder} for Feign that validates response objects using
     * Bean Validation annotations.
     *
     * @param validator the {@link Validator} used to enforce Bean Validation constraints
     * @return a {@link ValidatingDecoder} that wraps the default Spring decoder
     */
    @Bean
    public Decoder feignDecoder(Validator validator) {
        return new ValidatingDecoder(new SpringDecoder(HttpMessageConverters::new), validator);
    }

    /**
     * Configures the HTTP request options for Feign clients.
     * <p>
     * Sets the connect timeout, read timeout, and whether to follow HTTP redirects.
     * </p>
     *
     * @return {@link Request.Options} with custom timeout and redirect settings
     */
    @Bean
    public Request.Options options() {
        return new Request.Options(
                Duration.ofSeconds(5),   // connect timeout
                Duration.ofSeconds(5),   // read timeout
                true                     // follow redirects
        );
    }

    /**
     * Configures the retry policy for Feign clients.
     * <p>
     * This sets the initial interval, maximum interval, and maximum number of retry attempts
     * for failed HTTP requests.
     * </p>
     *
     * @return a {@link Retryer} configured for exponential backoff retries
     */
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(30000, 5000, 3); // initial interval 30s, max interval 5s, 3 attempts
    }
}

