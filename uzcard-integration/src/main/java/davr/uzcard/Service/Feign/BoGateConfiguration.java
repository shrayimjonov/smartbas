package davr.uzcard.Service.Feign;

import davr.uzcard.Exception.BadRequestException;
import davr.uzcard.Service.DecryptService;
import davr.uzcard.Service.ParamsService;
import feign.Client;
import feign.Logger;
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

@RequiredArgsConstructor
public class BoGateConfiguration {
    private final ParamsService paramsService;
    private final DecryptService decryptService;

    @Bean
    public ErrorDecoder errorDecoderBoGate() {
        return new ErrorDecoder.Default();
    }

    @Bean
    Logger.Level feignLoggerLevelBoGate() {
        return Logger.Level.FULL;
    }

    @Bean
    public Client feignClientBoGate() {
        return new Client.Default(getSSLSocketFactoryBoGate(), new NoopHostnameVerifier());
    }

    private SSLSocketFactory getSSLSocketFactoryBoGate() {
        try {
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            return sslContext.getSocketFactory();
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptorGenesis() {
        var login = decryptService.decrypt(paramsService.getValue("boGateLogin"));
        var password = decryptService.decrypt(paramsService.getValue("boGatePassword"));
        return new BasicAuthRequestInterceptor(login, password);
    }
}
