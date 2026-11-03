package soap.humo_gateway.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import soap.humo_gateway.dto.property.HumoCredentials;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.Objects;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;

@Component
@Slf4j
@RequiredArgsConstructor
public class SoapWebClient {
    private final HumoCredentials humoCredentials;


    public WebClient createClient() {
        ClientHttpConnector connector = null;
        try {

            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
            HttpClient httpClient = HttpClient
                    .create()
                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext))
                    .option(CONNECT_TIMEOUT_MILLIS, humoCredentials.getInfoTimeout().intValue())
                    .responseTimeout(Duration.ofMillis(humoCredentials.getActionTimeout()));
            connector = new ReactorClientHttpConnector(httpClient);
        } catch (SSLException e) {
            log.error("SSL exception: {}", e.getMessage());
        }

        return WebClient
                .builder()
                .codecs(codecs -> codecs
                        .defaultCodecs()
                        .maxInMemorySize(500 * 1024 * 1024))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML.toString())
                .filter(ExchangeFilterFunctions.basicAuthentication(humoCredentials.getLogin(), humoCredentials.getPassword()))
                .clientConnector(Objects.requireNonNull(connector))
                .build();
    }
}
