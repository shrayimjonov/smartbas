package soap.humo_gateway.service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import soap.humo_gateway.dto.property.HumoCredentials;
import soap.humo_gateway.config.SoapWebClient;
import soap.humo_gateway.dto.enums.HumoAction;
import soap.humo_gateway.dto.request.*;
import soap.humo_gateway.dto.response.ResponseData;
import soap.humo_gateway.exception.ConnectionError;
import soap.humo_gateway.exception.GatewayError;
import soap.humo_gateway.exception.HumoErrorResponse;
import soap.humo_gateway.service.InfoService;
import soap.humo_gateway.service.LogService;
import soap.humo_gateway.service.MethodsBody;

import javax.servlet.ServletRequest;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {
    private final SoapWebClient webClient;
    private final HumoCredentials humoCredentials;
    private final MethodsBody methodsBody;
    private final LogService logService;
    XmlMapper xmlMapper = new XmlMapper();

    @Override
    public Object humoHistory(ServletRequest servletRequest, HistoryRequest humoRequest) {
        var body = methodsBody.history(humoRequest);
       // logService.createLog(servletRequest, humoRequest.getSubSystem(), body);
        requestBodyLogger("humoHistory request: {}", body);
        return XmlToJson(sendRequest(body, HumoAction.HISTORY), "queryTransactionHistoryResponse");
    }

    @Override
    public Object getCardTransitAccount(ServletRequest servletRequest, TransitRequest request) {
        var body = methodsBody.transitAccount(request);
       // logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("getCardTransitAccount request: {}", body);
        return XmlToJson(sendRequest(body, HumoAction.LIST_ACCOUNTS), "listAccountsResponse");
    }

    @Override
    public Object getCardBalance(ServletRequest servletRequest, BalanceRequest request) {
        var body = methodsBody.getBalance(request);
     //   logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("getCardBalance request: {}", body);
        return balanceXml2(sendRequestToBalance(body));
    }

    @Override
    public Object deActivateCard(ServletRequest servletRequest, DeactivateRequest request) {
        var body = methodsBody.deActivateCard(request);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("deActivateCard request: {}", body);
        return XmlToJsonWithoutDetails(sendRequest(body, HumoAction.PAYMENT), "deactivateCardResponse");
    }

    @Override
    public Object activateCard(ServletRequest servletRequest, DeactivateRequest request) {
        var body = methodsBody.activateCard(request);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("activateCard request: {}", body);
        return XmlToJsonWithoutDetails(sendRequest(body, HumoAction.PAYMENT), "activateCardResponse");
    }

    @Override
    public Object transferDataToFo(ServletRequest servletRequest, TransferDataRequest request) {
        var body = methodsBody.transferDataToFo(request);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("transferDataToFo request: {}", body);
        return XmlToJsonWithoutDetails(sendRequest(body, HumoAction.PAYMENT), "transferDataToFoResponse");
    }

    @Override
    public Object getPinCount(ServletRequest servletRequest, PinCountReq request) {
        var body = methodsBody.getPinCounter(request);
     //   logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("getPinCount request: {}", body);
        return XmlToJsonWithoutItem(sendRequest(body, HumoAction.getCardPinTryCounter), "GetCardPinTryCounterResponse");
    }

    private static void requestBodyLogger(String s, String body) {
        log.info(s, body);
    }

    @Override
    public Object resetPinCount(ServletRequest servletRequest, ResetPinReq request) {
        var body = methodsBody.resetPinCounter(request);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("resetPinCount request: {}", body);
        return XmlToJsonWithoutDetails(sendRequest(body, HumoAction.getCardPinTryCounter), "resetPINCounterResponse");
    }


    private String sendRequest(String body, HumoAction action) {
        log.info("______Starting request______: {}", humoCredentials.getInfoUrl());
        try {
            ResponseEntity<String> resp = webClient.createClient()
                    .post()
                    .uri(URI.create(humoCredentials.getInfoUrl()))
                    .header("SOAPAction", action.getAction())
                    .body(BodyInserters.fromObject(body))
                    .exchange()
                    .flatMap(clientResponse -> clientResponse.toEntity(String.class))
                    .timeout(Duration.ofSeconds(humoCredentials.getInfoTimeout()))
                    .block();
            log.debug("humoInfo response: {}", resp);

            if (resp != null && !resp.getStatusCode().is2xxSuccessful())
                throw new GatewayError("Error from Humo processing side. " + resp.getBody());

            return Objects.requireNonNull(resp).getBody();
        } catch (Exception ex) {
            log.error("humoInfo error: {}", ex.getMessage());
            throw new ConnectionError(ex.getMessage());
        }
    }

    private String sendRequestToBalance(String body) {
        log.info("______Starting request______: {}", humoCredentials.getBalanceUrl());
        try {
            ResponseEntity<String> resp = webClient.createClient()
                    .post()
                    .uri(URI.create(humoCredentials.getBalanceUrl()))
                    .header("SOAPAction", HumoAction.GET_CARD_ACCOUNT_BALANCE.getAction())
                    .body(BodyInserters.fromObject(body))
                    .exchange()
                    .flatMap(clientResponse -> clientResponse.toEntity(String.class))
                    .timeout(Duration.ofSeconds(humoCredentials.getBalanceTimeout()))
                    .block();
            log.debug("humoBalance response: {}", resp);


            if (resp != null && !resp.getStatusCode().is2xxSuccessful())
                throw new GatewayError("Error from Humo processing side. " + resp.getBody());

            return Objects.requireNonNull(resp).getBody();
        } catch (Exception ex) {
            log.error("humoBalance error: {}", ex.getMessage());
            throw new ConnectionError(ex.getMessage());
        }
    }


    private Object XmlToJson(String response, String methodResponseName) {
        JSONObject one = new JSONObject(XML.toJSONObject(response).toString());

        if (!one.has("soapenv:Envelope"))
            throw new GatewayError("Error from Humo processing side. " + one);


        JSONObject two = one.getJSONObject("soapenv:Envelope");
        JSONObject three = two.getJSONObject("soapenv:Body");
        JSONObject four = three.getJSONObject(methodResponseName);

        if (!four.has("ResponseInfo"))
            throw new GatewayError("Error from Humo processing side. " + four);


        var responseClass = four.getJSONObject("ResponseInfo");

        if (!responseClass.get("response_code").equals(0))
            throw new HumoErrorResponse(responseClass.toString());

        JSONObject result = new JSONObject("{\"Details\": []}");

        if (methodResponseName.equals("queryTransactionHistoryResponse")) {
            if (four.has("Details") && !four.get("Details").equals("")) {
                getDetailsFromJson(four, result);
            }
        } else {
            getDetailsFromJson(four, result);
        }


        return result.toMap();

    }

    private void getDetailsFromJson(JSONObject four, JSONObject result) {
        JSONObject row1 = four.getJSONObject("Details").optJSONObject("row");

        if (row1 != null) {
            values(result, row1);
        } else {
            for (Object row : four.getJSONObject("Details").getJSONArray("row")) {
                if (!(row instanceof JSONObject)) continue;
                values(result, (JSONObject) row);
            }
        }
    }

    private Object XmlToJsonWithoutItem(String response, String methodResponseName) {
        JSONObject one = new JSONObject(XML.toJSONObject(response).toString());


        if (!one.has("soapenv:Envelope"))
            throw new GatewayError("Error from Humo processing side. " + one);

        JSONObject two = one.getJSONObject("soapenv:Envelope");
        JSONObject three = two.getJSONObject("soapenv:Body");
        JSONObject four = three.getJSONObject(methodResponseName);

        if (!four.has("ResponseInfo"))
            throw new GatewayError("Error from Humo processing side. " + four);


        var responseClass = four.getJSONObject("ResponseInfo");

        if (!responseClass.get("response_code").equals(0))
            throw new HumoErrorResponse(responseClass.toString());


        return four.getJSONObject("Details").toMap();

    }

    private Object XmlToJsonWithoutDetails(String response, String methodResponseName) {
        JSONObject one = new JSONObject(XML.toJSONObject(response).toString());

        if (!one.has("soapenv:Envelope"))
            throw new GatewayError("Error from Humo processing side. " + one);

        JSONObject two = one.getJSONObject("soapenv:Envelope");
        JSONObject three = two.getJSONObject("soapenv:Body");
        JSONObject four = three.getJSONObject(methodResponseName);

        if (!four.has("ResponseInfo"))
            throw new GatewayError("Error from Humo processing side. " + four);


        var responseClass = four.getJSONObject("ResponseInfo");

        if (!responseClass.get("response_code").equals(0))
            throw new HumoErrorResponse(responseClass.toString());


        return new ResponseData<>(true);

    }

    @SneakyThrows
    private Object balanceXml2(String response) {
        JsonNode node = xmlMapper.readTree(response.getBytes()).get("Body").get("getCardAccountsBalanceResponse");

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", new ObjectMapper().readValue(node.toString(), Object.class));
        return result;

    }

    private void values(JSONObject result, JSONObject row) {
        Map<Object, Object> values = new HashMap<>();
        for (var item : row.getJSONArray("item")) {
            if (!(item instanceof JSONObject)) continue;
            values.put(((JSONObject) item).get("name"), ((JSONObject) item).get("value"));
        }
        result.getJSONArray("Details").put(values);
    }


}
