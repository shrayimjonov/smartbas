package soap.humo_gateway.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ConnectTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import soap.humo_gateway.config.SoapWebClient;
import soap.humo_gateway.dto.enums.HumoAction;
import soap.humo_gateway.dto.property.HumoCredentials;
import soap.humo_gateway.dto.request.*;
import soap.humo_gateway.dto.response.ResponseData;
import soap.humo_gateway.dto.response.core.FaultDto;
import soap.humo_gateway.exception.ConnectionError;
import soap.humo_gateway.exception.GatewayError;
import soap.humo_gateway.exception.HumoErrorResponse;
import soap.humo_gateway.service.ActionService;
import soap.humo_gateway.service.LogService;
import soap.humo_gateway.service.MethodsBody;

import javax.servlet.ServletRequest;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionServiceImpl implements ActionService {
    private final MethodsBody methodsBody;
    private final SoapWebClient webClient;
    private final HumoCredentials humoCredentials;
    private final LogService logService;
    private final PinService pinService;
    @Value("${properties.max_amount}")
    private Long maxAmount;


    @Override
    public Object p2p(ServletRequest servletRequest, P2pRequest request) {
        var body = methodsBody.p2pTransfer(request);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("p2p request: {}", body);
        return XmlToJson(sendRequest(body, HumoAction.PAYMENT), "ebppif1:RequestResponse");
    }

    private static void requestBodyLogger(String methodName, String body) {
        log.debug(methodName, body);
    }

    @Override
    public Object debitCreate(ServletRequest servletRequest, DebitCreateRequest request) {
        var body = methodsBody.debitCreate(request);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("debitCreate request: {}", body);
        return XmlToJson(sendRequest(body, HumoAction.PAYMENT), "ebppif1:PaymentResponse");
    }

    @Override
    public Object debitConfirm(ServletRequest servletRequest, DebitConfirmRequest request) {
        var body = methodsBody.debitConfirm(request);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("debitConfirm request: {}", body);
        return XmlToJson(sendRequest(body, HumoAction.PAYMENT), "ebppif1:PaymentResponse");
    }

    @Override
    public Object credit(ServletRequest servletRequest, CreditRequest request) {
        String body;
        log.info("Replenishment card: {}", request.getPan2());
        if (request.getAmount() < maxAmount) {
            body = methodsBody.credit(request);
        } else {
            body = methodsBody.creditWithPassData(request);
        }
        logService.createLog(servletRequest, request.getSubSystem(), body);

        log.info("card credit request: {}", body);

        var response = sendRequest(body, HumoAction.PAYMENT);

        log.info("Card: {} replenish response: {}", request.getPan2(), response);

        return XmlToJson(response, "ebppif1:PaymentResponse");
    }

    @Override
    public Object executeTransaction(ServletRequest servletRequest, ExecuteTransactionRequest request) {
        log.info("executeTransaction request: {}", request);
        var body = methodsBody.executeTransaction(request);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("executeTransaction request: {}", body);
        return XmlToJsonExecuteTransaction(sendInfoRequest(body, HumoAction.EXECUTE_TRANSACTION));
    }

    @Override
    public Object pinAssign(ServletRequest servletRequest, PinAssignRequest request) {
        var body = methodsBody.assignPin(request, pinService.generatePinBlock(request.getSecurityCode(), request.getCard()));
        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("pinAssign request: {}", body);
        return XmlToJson(sendRequest(body), "cs:assignPinResponse");
    }


    private Object XmlToJson(String response, String methodResponseName) {
        JSONObject one = new JSONObject(XML.toJSONObject(response).toString());
        JSONObject two = one.getJSONObject("SOAP-ENV:Envelope");
        JSONObject three = two.getJSONObject("SOAP-ENV:Body");

        if (three.has("SOAP-ENV:Fault")) {
            return handleSoapFault(three);
        }

        JSONObject four = three.getJSONObject(methodResponseName);
        if (!methodResponseName.equals("cs:assignPinResponse")) {
            var handledItems = values(four.getJSONObject("details"));
            handledItems.put("paymentID", four.get("paymentID"));
            handledItems.put("paymentRef", four.get("paymentRef"));
            handledItems.put("action", four.get("action"));
            return handledItems;
        } else {
            return four.toMap();
        }

    }

    private Object XmlToJsonExecuteTransaction(String response) {
        JSONObject envelopeRoot = new JSONObject(XML.toJSONObject(response).toString());
        JSONObject envelope = getJsonObjectByLocalName(envelopeRoot, "Envelope");
        if (envelope == null) {
            throw new GatewayError("Error from Humo processing side. " + envelopeRoot);
        }

        JSONObject body = getJsonObjectByLocalName(envelope, "Body");
        if (body == null) {
            throw new GatewayError("Error from Humo processing side. " + envelope);
        }

        JSONObject fault = getJsonObjectByLocalName(body, "Fault");
        if (fault != null) {
            return handleSoapFault(fault);
        }

        JSONObject methodResponse = getJsonObjectByLocalName(body, "executeTransactionResponse");
        if (methodResponse == null) {
            throw new GatewayError("Error from Humo processing side. " + body);
        }

        JSONObject responseInfo = getJsonObjectByLocalName(methodResponse, "ResponseInfo");
        if (responseInfo == null) {
            throw new GatewayError("Error from Humo processing side. " + methodResponse);
        }

        if (!String.valueOf(responseInfo.get("response_code")).equals("0")) {
            throw new HumoErrorResponse(responseInfo.toString());
        }

        Map<Object, Object> result = detailsValues(methodResponse);
        Object internalNo = getValueByLocalName(methodResponse, "INTERNAL_NO");
        if (internalNo != null) {
            result.put("INTERNAL_NO", internalNo);
        }

        if (!result.containsKey("INTERNAL_NO")) {
            throw new GatewayError("INTERNAL_NO not found in Humo executeTransaction response. " + methodResponse);
        }

        return result;
    }

    private ResponseData<Object> handleSoapFault(JSONObject three) {
        try {
            JSONObject fault = three.has("SOAP-ENV:Fault") ? three.getJSONObject("SOAP-ENV:Fault") : three;
            var faultDto = new ObjectMapper().readValue(fault.toString(), FaultDto.class);
            return new ResponseData<>(false,
                    (long) faultDto.getDetail().getPaymentServerException().getError(),
                    faultDto.getDetail().getPaymentServerException().getDescription());
        } catch (Exception ex) {
            throw new HumoErrorResponse(three.toString());
        }
    }

    private Map<Object, Object> values(JSONObject row) {
        Map<Object, Object> values = new HashMap<>();
        for (Object item : row.getJSONArray("item")) {
            if (!(item instanceof JSONObject)) continue;
            values.put(((JSONObject) item).get("name"), ((JSONObject) item).get("value"));
        }
        return values;
    }

    private Map<Object, Object> detailsValues(JSONObject methodResponse) {
        Map<Object, Object> values = new HashMap<>();
        JSONObject details = getJsonObjectByLocalName(methodResponse, "Details");
        if (details == null) {
            return values;
        }

        Object row = getValueByLocalName(details, "row");
        if (row instanceof JSONObject) {
            addRowValues(values, (JSONObject) row);
        } else if (row instanceof JSONArray) {
            for (Object item : (JSONArray) row) {
                if (item instanceof JSONObject) {
                    addRowValues(values, (JSONObject) item);
                }
            }
        }

        return values;
    }

    private void addRowValues(Map<Object, Object> values, JSONObject row) {
        Object item = getValueByLocalName(row, "item");
        if (item instanceof JSONObject) {
            addItemValue(values, (JSONObject) item);
        } else if (item instanceof JSONArray) {
            for (Object rowItem : (JSONArray) item) {
                if (rowItem instanceof JSONObject) {
                    addItemValue(values, (JSONObject) rowItem);
                }
            }
        }
    }

    private void addItemValue(Map<Object, Object> values, JSONObject item) {
        if (item.has("name") && item.has("value")) {
            values.put(item.get("name"), item.get("value"));
        }
    }

    private JSONObject getJsonObjectByLocalName(JSONObject object, String localName) {
        Object value = getValueByLocalName(object, localName);
        return value instanceof JSONObject ? (JSONObject) value : null;
    }

    private Object getValueByLocalName(JSONObject object, String localName) {
        if (object.has(localName)) {
            return object.get(localName);
        }

        for (String key : object.keySet()) {
            if (key.equals(localName) || key.endsWith(":" + localName)) {
                return object.get(key);
            }
        }

        return null;
    }

    private String sendRequest(String body, HumoAction action) {
        log.info("______Starting request______: {}", humoCredentials.getPaymentUrl());
        try {
            ResponseEntity<String> resp = webClient.createClient()
                    .post()
                    .uri(URI.create(humoCredentials.getPaymentUrl()))
                    .header("SOAPAction", action.getAction())
                    .body(BodyInserters.fromObject(body))
                    .exchange()
                    .flatMap(clientResponse -> clientResponse.toEntity(String.class))
                    .timeout(Duration.ofSeconds(humoCredentials.getPaymentTimeout()))
                    .block();
            log.debug("humoAction response: {}", resp);
            return Objects.requireNonNull(resp).getBody();
        } catch (Exception e) {
            if (e.getCause() instanceof ConnectTimeoutException) {
                log.error("huMo action catch connection time out exception: {}", e.getMessage());
                throw new ConnectionError(e.getMessage());
            }
            log.error("humoAction error: {}", e.getMessage());
            throw new GatewayError(e.getMessage());
        }
    }

    private String sendInfoRequest(String body, HumoAction action) {
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
            log.debug("humo executeTransaction response: {}", resp);

            if (resp != null && !resp.getStatusCode().is2xxSuccessful()) {
                throw new GatewayError("Error from Humo processing side. " + resp.getBody());
            }

            return Objects.requireNonNull(resp).getBody();
        } catch (Exception e) {
            if (e.getCause() instanceof ConnectTimeoutException) {
                log.error("humo executeTransaction catch connection time out exception: {}", e.getMessage());
                throw new ConnectionError(e.getMessage());
            }
            log.error("humo executeTransaction error: {}", e.getMessage());
            throw new GatewayError(e.getMessage());
        }
    }

    private String sendRequest(String body) {
        log.info("______Starting request______: {}", humoCredentials.getPinUrl());
        try {
            ResponseEntity<String> resp = webClient.createClient()
                    .post()
                    .uri(URI.create(humoCredentials.getPinUrl()))
                    //   .header("SOAPAction", "")
                    .body(BodyInserters.fromObject(body))
                    .exchange()
                    .flatMap(clientResponse -> clientResponse.toEntity(String.class))
                    .timeout(Duration.ofSeconds(humoCredentials.getPaymentTimeout()))
                    .block();
            log.debug("humo PinAssign response: {}", resp);
            return Objects.requireNonNull(resp).getBody();
        } catch (Exception ex) {
            log.error("humo PinAssign error: {}", ex.getMessage());
            throw new ConnectionError(ex.getMessage());
        }
    }


}
