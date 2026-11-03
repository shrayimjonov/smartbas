package nci.billing.feign;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import nci.billing.model.property.LoanProps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
public class LoanFeignConfiguration {
    private final LoanProps loanProps;


    @Bean
    public ErrorDecoder errorDecoderLoan() {
        return new ErrorDecoder.Default();
    }

    @Bean
    public Logger.Level feignLoggerLevelLoan() {
        return Logger.Level.FULL;
    }

    @Bean
    @Profile("prod")
    public RequestInterceptor requestInterceptorProd() {
        return requestTemplate -> requestTemplate.header("Authorization", "Bearer " +
                loanProps.getRealToken());
    }

    @Bean
    @Profile("local")
    public RequestInterceptor requestInterceptorLocal() {
        return requestTemplate -> requestTemplate.header("Authorization", "Bearer " +
                loanProps.getTestToken());
    }


}
