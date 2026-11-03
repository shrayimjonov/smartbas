package soap.humo_gateway.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import soap.humo_gateway.dto.property.HumoCredentials;
import soap.humo_gateway.config.SoapWebClient;
import soap.humo_gateway.dto.enums.HumoAction;
import soap.humo_gateway.dto.request.limits.GetCardLimitBalanceRequest;
import soap.humo_gateway.dto.request.limits.GetCardLimitRequest;
import soap.humo_gateway.dto.request.limits.GetLimits;
import soap.humo_gateway.dto.request.limits.SetCardLimitRequest;
import soap.humo_gateway.dto.response.LimitListResponse;
import soap.humo_gateway.dto.response.LimitResponse;
import soap.humo_gateway.dto.response.ResponseData;
import soap.humo_gateway.exception.ConnectionError;
import soap.humo_gateway.exception.GatewayError;
import soap.humo_gateway.exception.HumoErrorResponse;
import soap.humo_gateway.exception.InvalidData;
import soap.humo_gateway.service.LimitsMethodsBody;
import soap.humo_gateway.service.LimitsService;
import soap.humo_gateway.service.LogService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class LimitServiceImpl implements LimitsService {
    private final LimitsMethodsBody methodsBody;
    private final LogService logService;
    private final HumoCredentials humoCredentials;
    private final SoapWebClient webClient;
    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public Object getLimitsType(GetLimits request, HttpServletRequest servletRequest) {
        var body = methodsBody.getLimitsRefData();
     //   logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("getLimitsType request: {}", body);
        return limitXmlToJson(sendRequestToLimit(body));
    }

    @Override
    public Object getCardLimits(GetCardLimitRequest request, HttpServletRequest servletRequest) {
        var body = methodsBody.getCardLimits(request);
      //  logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("getCardLimits request: {}", body);
        return limitXmlToJson(sendRequestToLimit(body));
    }

    @Override
    public Object getCardLimitsBalance(GetCardLimitBalanceRequest request, HttpServletRequest servletRequest) {
        var body = methodsBody.getCardLimitBalance(request);
      //  logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("getCardLimitsBalance request: {}", body);
        return limitXmlToJson(sendRequestToLimit(body));
    }

    @Override
    @SneakyThrows
    public Object setCardLimit(SetCardLimitRequest request, HttpServletRequest servletRequest) {
        String body;

        if (request.getLimits() == null || request.getLimits().isEmpty())
            body = methodsBody.deleteLimitBalance(request);
        else
            body = methodsBody.setLimitBalance(request, checkCardLimitRequest(request));

        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("setCardLimit request: {}", body);
        var preparedResponse = xmlMapper.readValue(sendRequestToLimit(body), LimitResponse.class);
        return new ResponseData<>(preparedResponse.getBody().getSetLimitsResponse().getLimitEvent());


    }

    public String checkCardLimitRequest(SetCardLimitRequest request) {


        StringBuilder newString = new StringBuilder();


        Set<String> duplicates = new LinkedHashSet<>();
        Set<String> uniques = new HashSet<>();

        for (var t : request.getLimits()) {
            if (!uniques.add(t.get("type"))) {
                duplicates.add(t.get("type"));
            }

            if (!duplicates.isEmpty())
                throw new InvalidData("Limits must be unique!");
        }


        for (var limit : request.getLimits()) {
            var type = limit.getOrDefault("type", null);

            if (!limit.get("amount").matches("^[0-9]*$"))
                throw new InvalidData("Limit amount must be consist of digits");

            var amount = Long.parseLong(limit.get("amount"));
            var name = limit.getOrDefault("name", null);
            var dateFrom = limit.getOrDefault("dateFrom", null);
            var dateTo = limit.getOrDefault("dateTo", null);

            if (type == null)
                throw new InvalidData("Card Limit request has not type field");

            if (amount < 0)
                throw new InvalidData("Card Limit request has not type amount field");

            if (limit.get("name") == null)
                throw new InvalidData("Card Limit request has not name field");

            var dateRegex = "(20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])";

            if (!limit.get("dateFrom").matches(dateRegex) || !limit.get("dateTo").matches(dateRegex))
                throw new InvalidData("Card Limit request date fields incorrectly filled");

            newString.append("<limit>\n" + "                    <subject_value>").append(request.getCard()).append("</subject_value>\n").append("                    <limit_type>").append(type).append("</limit_type>\n").append("                    <limit_value>").append(amount).append("</limit_value>\n").append("                    <limit_name>").append(name).append("</limit_name>\n").append("                    <limit_date_from>").append(dateFrom).append("000000").append("</limit_date_from>\n").append("                    <limit_date_to>").append(dateTo).append("235959").append("</limit_date_to>\n").append("                </limit>\n");

        }
        return newString.toString();
    }


    private String sendRequestToLimit(String body) {
        log.info("______Starting request______: {}", humoCredentials.getLimitUrl());
        try {
            ResponseEntity<String> resp = webClient.createClient()
                    .post()
                    .uri(URI.create(humoCredentials.getLimitUrl()))
                    .header("SOAPAction", HumoAction.LIMITS.getAction())
                    .bodyValue(body)
                    .exchange()
                    .flatMap(clientResponse -> clientResponse.toEntity(String.class))
                    .timeout(Duration.ofSeconds(humoCredentials.getLimitTimeout()))
                    .block();
            log.info("Humo Limit service response: {}", resp);


            if (resp != null && !resp.getStatusCode().is2xxSuccessful())
                throw new GatewayError("Error from Humo processing side. " + resp.getBody());

            return Objects.requireNonNull(resp).getBody();
        } catch (Exception ex) {
            log.error("Humo Limit service error: {}", ex.getMessage());
            throw new ConnectionError(ex.getMessage());
        }
    }

    @SneakyThrows
    private JsonNode limitXmlToJson(String response) {
        return xmlMapper.readTree(response.getBytes()).get("Body");

    }

    private static void requestBodyLogger(String s, String body) {
        log.info(s, body);
    }

    @Override
    public LimitListResponse getCardLimitsV2(GetCardLimitRequest request) {
        var body = methodsBody.getCardLimits(request);

        requestBodyLogger("getCardLimits request: {}", body);
        try {
            return xmlMapper.readValue(sendRequestToLimit(body), LimitListResponse.class);
        } catch (JsonProcessingException e) {
            throw new HumoErrorResponse(e);
        }
    }

}
