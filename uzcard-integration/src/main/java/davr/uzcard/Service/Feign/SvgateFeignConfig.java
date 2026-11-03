package davr.uzcard.Service.Feign;


import davr.uzcard.Exception.BadRequestException;
import davr.uzcard.Service.DecryptService;
import davr.uzcard.Service.ParamsService;
import feign.Client;
import feign.Logger;
import feign.Request;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class SvgateFeignConfig {
    private final ParamsService paramsService;
    private final DecryptService decryptService;

    @Bean
    Logger.Level feignLoggerLevelSVGate() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default();
    }

    @Bean
    public Client feignClient() {
        return new Client.Default(getSSLSocketFactory(), new NoopHostnameVerifier());
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            return sslContext.getSocketFactory();
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptorSVGate() {
        var login = decryptService.decrypt(paramsService.getValue("svGateLogin"));
        var password = decryptService.decrypt(paramsService.getValue("svGatePassword"));
        return new BasicAuthRequestInterceptor(login, password);
    }

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                2, TimeUnit.MINUTES,
                60, TimeUnit.SECONDS,
                true
        );
    }
}
