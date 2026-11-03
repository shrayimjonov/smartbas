package soap.humo_gateway.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;
import soap.humo_gateway.dto.property.HumoCredentials;
import soap.humo_gateway.config.SoapWebClient;
import soap.humo_gateway.dto.ErrorDto;
import soap.humo_gateway.dto.enums.HumoAction;
import soap.humo_gateway.dto.request.*;
import soap.humo_gateway.dto.response.ResponseData;
import soap.humo_gateway.dto.response.core.FaultDto;
import soap.humo_gateway.exception.ConnectionError;
import soap.humo_gateway.exception.HumoErrorResponse;
import soap.humo_gateway.service.LogService;
import soap.humo_gateway.service.MethodsBody;
import soap.humo_gateway.service.PaymentService;

import javax.servlet.ServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentImpl implements PaymentService {
    private final SoapWebClient webClient;
    private final HumoCredentials humoCredentials;
    private final MethodsBody methodsBody;
    private final LogService logService;

    @Override
    public Object getPaymentByPaymentId(ServletRequest servletRequest, PaymentRequest request) {
        var body = methodsBody.paymentById(request);
        requestBodyLogger("getPaymentByPaymentId request: {}", body);
     //   logService.createLog(servletRequest, request.getSubSystem(), body);
        return XmlToJson(sendRequest(body, HumoAction.GET_PAYMENT), "ebppif1:GetPaymentResponse");
    }

    private static void requestBodyLogger(String s, String body) {
        log.info(s, body);
    }

    @Override
    public Object getPaymentByExternalId(ServletRequest servletRequest, RefNumRequest request) {
        var body = methodsBody.paymentByExternalId(request);
        requestBodyLogger("getPaymentByExternalId request: {}", body);
     //   logService.createLog(servletRequest, request.getSubSystem(), body);
        return XmlToJson(sendRequest(body, HumoAction.GET_PAYMENT), "ebppif1:GetPaymentResponse");
    }

    @Override
    public Object cancelActiveById(ServletRequest servletRequest, CancelRequest request) {
        var body = methodsBody.cancelActiveById(request);
        requestBodyLogger("cancelActiveById request: {}", body);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        return XmlToJsonPayment(sendRequest(body, HumoAction.REVERT), "ebppif1:CancelRequestResponse");
    }

    @Override
    public Object cancelActiveByRefNum(ServletRequest servletRequest, CancelRefRequest request) {
        var body = methodsBody.cancelActiveByRefNum(request);
        requestBodyLogger("cancelActiveByRefNum request: {}", body);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        return XmlToJsonPayment(sendRequest(body, HumoAction.REVERT), "ebppif1:CancelRequestResponse");
    }

    @Override
    public Object returnFinishedById(ServletRequest servletRequest, ReturnRequest request) {
        var body = methodsBody.returnFinishedById(request);
        requestBodyLogger("returnFinishedById request: {}", body);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        return XmlToJsonPayment(sendRequest(body, HumoAction.REVERT), "ebppif1:ReturnPaymentResponse");
    }

    @Override
    public Object returnFinishedByRefNum(ServletRequest servletRequest, ReturnRefRequest request) {
        var body = methodsBody.returnFinishedByRefNum(request);
        requestBodyLogger("returnFinishedByRefNum request: {}", body);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        return XmlToJsonPayment(sendRequest(body, HumoAction.REVERT), "ebppif1:ReturnPaymentResponse");
    }

    @Override
    public Object reconciliationCreate(ServletRequest servletRequest, RecoCreateRequest request) {
        var body = methodsBody.reconcileEPosCreate(request);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("reconciliationCreate request: {}", body);
        return XmlToJson(sendRequest(body, HumoAction.PAYMENT), "ebppif1:PaymentResponse");
    }

    @Override
    public Object reconciliationFinish(ServletRequest servletRequest, RecoFinishRequest request) {
        var body = methodsBody.reconcileEPosFinish(request);
        logService.createLog(servletRequest, request.getSubSystem(), body);
        requestBodyLogger("reconciliationFinish request: {}", body);
        return XmlToJson(sendRequest(body, HumoAction.PAYMENT), "ebppif1:PaymentResponse");
    }


    private String sendRequest(String body, HumoAction action) {
        log.info("______Starting request______: {}", humoCredentials.getPaymentUrl());
        try {
            var response = webClient.createClient()
                    .post()
                    .uri(URI.create(humoCredentials.getPaymentUrl()))
                    .header("SOAPAction", action.getAction())
                    .bodyValue(body)
                    .exchangeToMono(res -> res.bodyToMono(String.class))
                    .block();
            log.debug("humoPayment response: {}", response);
            return Objects.requireNonNull(response);
        } catch (Exception ex) {
            log.error("humoPayment error: {}", ex.getMessage());
            throw new ConnectionError(ex.getMessage());
        }
    }

    private Object XmlToJson(String response, String methodResponseName) {
        JSONObject one = new JSONObject(XML.toJSONObject(response).toString());
        JSONObject two = one.getJSONObject("SOAP-ENV:Envelope");
        JSONObject three = two.getJSONObject("SOAP-ENV:Body");

        if (three.has("SOAP-ENV:Fault")) {
            return handleSoapFault(three);
        }

        JSONObject four = three.getJSONObject(methodResponseName);
        var handledItems = values(four.getJSONObject("details"));
        handledItems.put("paymentID", four.get("paymentID"));
        handledItems.put("paymentRef", four.get("paymentRef"));
        handledItems.put("status", four.get("status"));
        return handledItems;
    }


    private ResponseData<Object> handleSoapFault(JSONObject three) {
        try {
            var faultDto = new ObjectMapper().readValue(three.getJSONObject("SOAP-ENV:Fault").toString(), FaultDto.class);
            return new ResponseData<>(false,
                    (long) faultDto.getDetail().getPaymentServerException().getError(),
                    faultDto.getDetail().getPaymentServerException().getDescription());
        } catch (Exception ex) {
            throw new HumoErrorResponse(three.getJSONObject("SOAP-ENV:Fault").toString());
        }
    }

    private Object XmlToJsonPayment(String response, String methodResponseName) {
        JSONObject one = new JSONObject(XML.toJSONObject(response).toString());
        JSONObject two = one.getJSONObject("SOAP-ENV:Envelope");
        JSONObject three = two.getJSONObject("SOAP-ENV:Body");
        if (three.has("SOAP-ENV:Fault"))
            return handleSoapFault(three);

        // return three.get(methodResponseName);
        return ErrorDto.builder()
                .build();

    }


    private Map<Object, Object> values(JSONObject row) {
        Map<Object, Object> values = new HashMap<>();
        for (Object item : row.getJSONArray("item")) {
            if (!(item instanceof JSONObject)) continue;
            values.put(((JSONObject) item).get("name"), ((JSONObject) item).get("value"));
        }
        return values;
    }


}
