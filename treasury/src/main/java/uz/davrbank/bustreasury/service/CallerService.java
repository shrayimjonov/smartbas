package uz.davrbank.bustreasury.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uz.davrbank.bustreasury.aggregate.config.HeadConfig;
import uz.davrbank.bustreasury.aggregate.dto.response.ResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.ResultDTO;
import uz.davrbank.bustreasury.exeption.AuthTokenException;
import uz.davrbank.bustreasury.exeption.ConnectionException;
import uz.davrbank.bustreasury.exeption.NotFoundException;
import uz.davrbank.bustreasury.exeption.ThirdPartyException;

import java.util.List;

@Service
public class CallerService {
    private static final Logger LOG = LoggerFactory.getLogger(CallerService.class);
    private final WebClient webClient;
    private final Gson gson;
    private final HeadConfig headerConfig;
    private final ObjectMapper objectMapper;

    @Value("${gateway.url.end}")
    private String gatewayUrl;

    @Value("${gateway-status-check.check-url.end}")
    private String checkGatewayUrl;

    public CallerService(WebClient webClient, Gson gson, HeadConfig headerConfig, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.gson = gson;
        this.headerConfig = headerConfig;
        this.objectMapper = objectMapper;
    }

    public <R> ResultDTO SendDoc2Gateway(R request, Class<R> requestType) {
        LOG.info("REQUEST: call To Egov Server By Rent Service : {}", gson.toJson(request));
        return sendRequest(gatewayUrl, request, requestType);
    }


    public <R> ResultDTO SendStatusCheck2Gateway(R request, Class<R> requestType) {
        LOG.info("STATUS CHECK REQUEST: call To Egov Server for status : {}", gson.toJson(request));
        return sendRequest(checkGatewayUrl, request, requestType);
    }

    private <R> ResultDTO sendRequest(String url, R request, Class<R> requestType) {
        try {
            return webClient
                    .method(HttpMethod.POST)
                    .uri(url)
                    .headers(header -> header.addAll(headerConfig.getHeaders()))
                    .body(Mono.just(request), requestType)
                    .exchangeToMono(response -> {
                        int statusCode = response.statusCode().value();
                        LOG.info("RESPONSE FROM SERVER STATUS  :  {} for URL: {}", statusCode, url);

                        if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                            throw new AuthTokenException("Authentication failed");
                        } else if (statusCode == HttpStatus.TOO_MANY_REQUESTS.value()) {
                            throw new ThirdPartyException("Too many requests");
                        } else if (statusCode == HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value()) {
                            throw new ThirdPartyException("Requested range not satisfiable");
                        } else if (statusCode == HttpStatus.NOT_FOUND.value() || statusCode == HttpStatus.NO_CONTENT.value()) {
                            throw new NotFoundException("Data Not found");
                        } else if (statusCode != HttpStatus.OK.value()) {
                            throw new ConnectionException("Error occurred: " + statusCode);
                        }

                        return response.bodyToMono(String.class).flatMap(rawResponse -> {
                            LOG.info("Raw response body for URL {}: {}", url, rawResponse);

                            try {
                                List<ResponseDTO> responses = objectMapper.readValue(rawResponse, new TypeReference<List<ResponseDTO>>() {});
                                return Mono.just(new ResultDTO(responses));
                            } catch (Exception e) {
                                LOG.warn("List<ResponseDTO>  muvaffaqiyatsiz: {}. Bitta ResponseDTO sinab ko‘riladi", e.getMessage());
                                ResponseDTO singleResponse = null;
                                try {
                                    singleResponse = objectMapper.readValue(rawResponse, ResponseDTO.class);
                                } catch (JsonProcessingException ex) {
                                    throw new RuntimeException(ex);
                                }
                                return Mono.just(new ResultDTO(List.of(singleResponse)));
                            }
                        });
                    })
                    .block();
        } catch (AuthTokenException | NotFoundException | ThirdPartyException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.error("ERROR OCCURRED for URL {}: {}", url, ex.getMessage(), ex);
            throw new ConnectionException(ex);
        }
    }
}
