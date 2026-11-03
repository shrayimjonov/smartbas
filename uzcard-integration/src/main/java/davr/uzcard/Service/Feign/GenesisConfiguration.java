package davr.uzcard.Service.Feign;

import davr.uzcard.Exception.BadRequestException;
import davr.uzcard.Exception.SvgateErrorDecoder;
import davr.uzcard.Service.DecryptService;
import davr.uzcard.Service.ParamsService;
import feign.Client;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

@RequiredArgsConstructor
public class GenesisConfiguration {

    private final ParamsService paramsService;
    private final DecryptService decryptService;

    @Bean
    public ErrorDecoder errorDecoder() {
        return new SvgateErrorDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Client feignClient() {
        return new Client.Default(getSSLSocketFactory(), new NoopHostnameVerifier());
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllX509TrustManager()}, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((string, ssls) -> true);
            return sc.getSocketFactory();
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    public static class TrustAllX509TrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
                                       String authType) {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
                                       String authType) {
        }

    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptorGenesis() {
        var login = decryptService.decrypt(paramsService.getValue("autoPayLogin"));
        var password = decryptService.decrypt(paramsService.getValue("autoPayPassword"));
        return new BasicAuthRequestInterceptor(login, password);
    }

}
