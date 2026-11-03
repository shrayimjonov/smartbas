package uz.devops.humoMiddleware.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uz.devops.humoMiddleware.config.HumoMiddlewareProperties;
import uz.devops.humoMiddleware.dto.basic.ChangePhoneNumber;
import uz.devops.humoMiddleware.dto.basic.CustomerActivate;
import uz.devops.humoMiddleware.dto.basic.ParamMiddleware;
import uz.devops.humoMiddleware.dto.basic.ResultMiddleware;
import uz.devops.humoMiddleware.dto.enums.MethodUrls;
import uz.devops.humoMiddleware.dto.request.*;
import uz.devops.humoMiddleware.dto.response.*;
import uz.devops.humoMiddleware.service.HumoMiddlewareService;
import uz.javlon.commons.base.SimpleResultData;

@Slf4j
@Service(HumoMiddlewareService.NAME)
@ConditionalOnProperty(
    prefix = "humo-middleware",
    name = "simulate",
    havingValue = "false"
)
public class HumoMiddlewareServiceImpl implements HumoMiddlewareService {
    private static final String BEARER = "Bearer ";
    private static final String HEADER_NAME = "Authorization";
    private final HumoMiddlewareProperties humoMiddlewareProperties;
    private final HttpHeaders httpHeaders;
    private final RestTemplate humoMiddlewareRestTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public HumoMiddlewareServiceImpl(HumoMiddlewareProperties humoMiddlewareProperties, @Qualifier("humoMiddlewareRestTemplate") RestTemplate humoMiddlewareRestTemplate, ObjectMapper objectMapper) {
        this.humoMiddlewareProperties = humoMiddlewareProperties;
        this.humoMiddlewareRestTemplate = humoMiddlewareRestTemplate;
        this.objectMapper = objectMapper;

        httpHeaders = getHeaders();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HEADER_NAME, BEARER + humoMiddlewareProperties.getToken());
        return httpHeaders;
    }

    @Override
    public SimpleResultData<GetRealPanResponseDTO> getRealPan(GetRealPanRequestDTO request) {
        try {
            String pan = verifyAndReturnRealPan(request.getMaskedPan());
            if (!StringUtils.isEmpty(pan)) return new SimpleResultData<>(new GetRealPanResponseDTO(pan));

            HttpEntity<GetRealPanRequestDTO> requestEntity = new HttpEntity<>(request, httpHeaders);
            String url = humoMiddlewareProperties.getIsTestHumo() ? humoMiddlewareProperties.getUrl() + MethodUrls.GET_REAL_PAN.getTestUrl() + request.getMaskedPan() : humoMiddlewareProperties.getUrl() + MethodUrls.GET_REAL_PAN.getProdUrl() + request.getMaskedPan();
            log.info("begin getRealPan method with params {}", request);
            ResponseEntity<GetRealPanResponseDTO> responseEntity = humoMiddlewareRestTemplate.exchange(url, HttpMethod.GET, requestEntity, GetRealPanResponseDTO.class);
            return new SimpleResultData<>(responseEntity.getBody());
        } catch (Exception e) {
            log.warn("getRealPan method handle exception {}", e.getMessage());

            if (e instanceof HttpClientErrorException) {
                return new SimpleResultData<>(false, null, new RuntimeException(((HttpClientErrorException) e).getResponseBodyAsString()));
            }

            log.warn("getRealPan method handle exception {}", e.getMessage());
            return new SimpleResultData<>(false, null, new RuntimeException(e.getMessage()));
        }
    }

    @Override
    public SimpleResultData<GetMaskedPanResponseDTO> getMaskedPan(GetMaskedPanRequestDTO request) {
        try {
            String pan = verifyAndReturnRealPan(request.getPan());
            if (!StringUtils.isEmpty(pan)) return new SimpleResultData<>(new GetMaskedPanResponseDTO(pan));

            HttpEntity<GetMaskedPanRequestDTO> requestEntity = new HttpEntity<>(request, httpHeaders);
            String url = humoMiddlewareProperties.getIsTestHumo() ? humoMiddlewareProperties.getUrl() + MethodUrls.GET_MASKED_PAN.getTestUrl() + request.getPan() : humoMiddlewareProperties.getUrl() + MethodUrls.GET_MASKED_PAN.getProdUrl() + request.getPan();
            log.info("begin getRealPan method with params {}", request);
            ResponseEntity<GetMaskedPanResponseDTO> responseEntity = humoMiddlewareRestTemplate.exchange(url, HttpMethod.GET, requestEntity, GetMaskedPanResponseDTO.class);
            return new SimpleResultData<>(responseEntity.getBody());
        } catch (Exception e) {
            log.warn("getMaskedPan method handle exception {}", e.getMessage());
            if (e instanceof HttpClientErrorException) {
                return new SimpleResultData<>(false, null, new RuntimeException(((HttpClientErrorException) e).getResponseBodyAsString()));
            }
            log.warn("getMaskedPan method handle exception {}", e.getMessage());
            return new SimpleResultData<>(false, null, new RuntimeException(e.getMessage()));
        }
    }

    private String verifyAndReturnRealPan(String pan) {
        String regex = "^(9860|5555)[0-9]{12}";
        if (!StringUtils.isEmpty(pan) && pan.matches(regex)) {
            return pan;
        }
        return null;
    }

    @Override
    public SimpleResultData<GetCardInfoResponseDTO> getCardInfo(GetCardInfoRequestDTO request) {
        try {
            HttpEntity<GetCardInfoRequestDTO> requestEntity = new HttpEntity<>(request, httpHeaders);
            String url = humoMiddlewareProperties.getIsTestHumo() ? humoMiddlewareProperties.getUrl() + MethodUrls.GET_CARD_INFO.getTestUrl() : humoMiddlewareProperties.getUrl() + MethodUrls.GET_CARD_INFO.getProdUrl();
            log.info("begin getCardInfo method with params {}", request);
            ResponseEntity<GetCardInfoResponseDTO> responseEntity = humoMiddlewareRestTemplate.exchange(url, HttpMethod.POST, requestEntity, GetCardInfoResponseDTO.class);
            return new SimpleResultData<>(responseEntity.getBody());
        } catch (Exception e) {
            log.warn("getCardInfo method handle exception {}", e.getMessage());
            if (e instanceof HttpClientErrorException) {
                return new SimpleResultData<>(false, null, new RuntimeException(((HttpClientErrorException) e).getResponseBodyAsString()));
            }
            log.warn("getCardInfo method handle exception {}", e.getMessage());
            return new SimpleResultData<>(false, null, new RuntimeException(e.getMessage()));
        }
    }

    @Override
    public SimpleResultData<ResultMiddleware<ClientActivateResponseDTO>> clientActivate(CustomerActivate request) {
        SimpleResultData<ResultMiddleware<ClientActivateResponseDTO>> resultData = sendAndReceive(request, ClientActivateResponseDTO.class, MethodUrls.CUSTOMER_ACTIVATE);
        if (resultData.isSuccess()) {
            resultData.getData().setResult(objectMapper.convertValue(resultData.getData().getResult(), ClientActivateResponseDTO.class));
        }
        return resultData;
    }

    public <RESPONSE, REQUEST> SimpleResultData<ResultMiddleware<RESPONSE>> sendAndReceive(REQUEST request, Class<RESPONSE> response, MethodUrls method) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), httpHeaders);
            log.info("begin {} method with params {}", method.getMethodName(), request);
            String url = humoMiddlewareProperties.getIsTestHumo() ? humoMiddlewareProperties.getUrl() + method.getTestUrl() : humoMiddlewareProperties.getUrl() + method.getProdUrl();
            ResponseEntity<? extends ResultMiddleware<RESPONSE>> responseEntity = humoMiddlewareRestTemplate.exchange(url, HttpMethod.POST, requestEntity, getResponseType(response));
            return new SimpleResultData<>(responseEntity.getBody());
        } catch (Exception e) {
            log.warn("{} method handle exception {}", method.getMethodName(), e.getMessage());
            if (e instanceof HttpClientErrorException) {
                return new SimpleResultData<>(false, null, new RuntimeException(((HttpClientErrorException) e).getResponseBodyAsString()));
            }
            log.warn("{} method handle exception {}", method.getMethodName(), e.getMessage());
            return new SimpleResultData<>(false, null, new RuntimeException(e.getMessage()));
        }
    }


    @Override
    public SimpleResultData<ResultMiddleware<ClientInfo>> getClientByHumoId(ParamMiddleware<ClientRequestDTO> request) {
        return sendAndReceive(request, ClientInfo.class, MethodUrls.CUSTOMER);
    }

    public SimpleResultData<ResultMiddleware<GetClientsResponseDTO>> getClientByPhone(ParamMiddleware<CustomerListRequestDTO> request) {
        SimpleResultData<ResultMiddleware<GetClientsResponseDTO>> resultData = sendAndReceive(request, GetClientsResponseDTO.class, MethodUrls.CUSTOMER_LIST);
        if (resultData.isSuccess()) {
            resultData.getData().setResult(objectMapper.convertValue(resultData.getData().getResult(), GetClientsResponseDTO.class));
        }
        return resultData;
    }

    public SimpleResultData<ResultMiddleware<ClientDeactivateResponseDTO>> clientDeactivate(ParamMiddleware<CustomerDeactivateRequestDTO> request) {
        SimpleResultData<ResultMiddleware<ClientDeactivateResponseDTO>> resultData = sendAndReceive(request, ClientDeactivateResponseDTO.class, MethodUrls.CUSTOMER_DEACTIVATE);
        if (resultData.isSuccess()) {
            resultData.getData().setResult(objectMapper.convertValue(resultData.getData().getResult(), ClientDeactivateResponseDTO.class));
        }
        return resultData;
    }

    public SimpleResultData<ResultMiddleware<ChangePhoneNumberResponseDTO>> changeSmsInfoPhoneNumber(ChangePhoneNumber request) {
        SimpleResultData<ResultMiddleware<ChangePhoneNumberResponseDTO>> resultData = sendAndReceive(request, ChangePhoneNumberResponseDTO.class, MethodUrls.CHANGE_PHONE_NUMBER);
        if (resultData.isSuccess()) {
            resultData.getData().setResult(objectMapper.convertValue(resultData.getData().getResult(), ChangePhoneNumberResponseDTO.class));
        }
        return resultData;
    }

    public SimpleResultData<ResultMiddleware<ChangeSmsInfoLangResponseDTO>> changeSmsInfoLanguage(ParamMiddleware<ChangeSmsInfoLangRequestDTO> request) {
        SimpleResultData<ResultMiddleware<ChangeSmsInfoLangResponseDTO>> resultData = sendAndReceive(request, ChangeSmsInfoLangResponseDTO.class, MethodUrls.CHANGE_MESSAGE_LANG);
        if (resultData.isSuccess()) {
            resultData.getData().setResult(objectMapper.convertValue(resultData.getData().getResult(), ChangeSmsInfoLangResponseDTO.class));
        }
        return resultData;
    }

    @Override
    public SimpleResultData<ResultMiddleware<ClientRemoveCardResponseDTO>> clientRemoveCard(ParamMiddleware<CustomerRemoveCardRequestDTO> request) {
        SimpleResultData<ResultMiddleware<ClientRemoveCardResponseDTO>> resultData = sendAndReceive(request, ClientRemoveCardResponseDTO.class, MethodUrls.CUSTOMER_REMOVE_CARD);
        if (resultData.isSuccess()) {
            resultData.getData().setResult(objectMapper.convertValue(resultData.getData().getResult(), ClientRemoveCardResponseDTO.class));
        }
        return resultData;
    }

    @Override
    public SimpleResultData<ResultMiddleware<CustomerEditCardResponseDTO>> clientEditCard(ParamMiddleware<EditCardRequestDTO> request) {
        SimpleResultData<ResultMiddleware<CustomerEditCardResponseDTO>> resultData = sendAndReceive(request, CustomerEditCardResponseDTO.class, MethodUrls.CUSTOMER_EDIT_CARD);
        if (resultData.isSuccess()) {
            resultData.getData().setResult(objectMapper.convertValue(resultData.getData().getResult(), CustomerEditCardResponseDTO.class));
        }
        return resultData;
    }

    @Override
    public SimpleResultData<ResultMiddleware<RetrieveCardResponseDTO>> retrieveCard(ParamMiddleware<RetrieveCardRequestDTO> request) {
        SimpleResultData<ResultMiddleware<RetrieveCardResponseDTO>> resultData = sendAndReceive(request, RetrieveCardResponseDTO.class, MethodUrls.IIA_CS_CARD_V2);
        if (resultData.isSuccess()) {
            resultData.getData().setResult(objectMapper.convertValue(resultData.getData().getResult(), RetrieveCardResponseDTO.class));
        }
        return resultData;
    }

    @Override
    public SimpleResultData<ResultMiddleware<RetrieveCardV3ResponseDTO>> retrieveCardV3(ParamMiddleware<RetrieveCardV3RequestDTO> request) {
        SimpleResultData<ResultMiddleware<RetrieveCardV3ResponseDTO>> simpleResultData = sendAndReceive(request, RetrieveCardV3ResponseDTO.class, MethodUrls.IIA_CS_CARD_V3);
        if (simpleResultData.isSuccess()) {
            simpleResultData.getData()
                .setResult(objectMapper.convertValue(simpleResultData.getData().getResult(), RetrieveCardV3ResponseDTO.class));
        }
        return simpleResultData;
    }

    @Override
    public SimpleResultData<ResultMiddleware<GetClientCardsByPassportResponseDTO>> getClientCardsByPassport(ParamMiddleware<GetCardsByPassportRequestDTO> request) {
        SimpleResultData<ResultMiddleware<GetClientCardsByPassportResponseDTO>> resultData = sendAndReceive(request, GetClientCardsByPassportResponseDTO.class, MethodUrls.CUSTOMER_CARDS_BY_PASSPORT);
        if (resultData.isSuccess()) {
            resultData.getData().setResult(objectMapper.convertValue(resultData.getData().getResult(), GetClientCardsByPassportResponseDTO.class));
        }
        return resultData;
    }

    @Override
    public SimpleResultData<ResultMiddleware<GetClientCardsByPinflResponseDTO>> getClientCardsByPinfl(ParamMiddleware<GetCardsByPinflRequestDTO> request) {
        SimpleResultData<ResultMiddleware<GetClientCardsByPinflResponseDTO>> resultData = sendAndReceive(request, GetClientCardsByPinflResponseDTO.class, MethodUrls.CUSTOMER_CARDS_BY_PERSON_CODE);
        if (resultData.isSuccess()) {
            resultData.getData().setResult(objectMapper.convertValue(resultData.getData().getResult(), GetClientCardsByPinflResponseDTO.class));
        }
        return resultData;
    }

    @Override
    public SimpleResultData<ResultMiddleware<SetExchangeRatesResponseDTO>> setExchangeRate(ParamMiddleware<SetExchangeRateRequestDTO> request) {
        SimpleResultData<ResultMiddleware<SetExchangeRatesResponseDTO>> resultData = sendAndReceive(request, SetExchangeRatesResponseDTO.class, MethodUrls.CCR2_EXCHANGE_RATES);
        if (resultData.isSuccess()) {
            resultData.getData().setResult(objectMapper.convertValue(resultData.getData().getResult(), SetExchangeRatesResponseDTO.class));
        }
        return resultData;
    }

    private <TResponse> ParameterizedTypeReference<ResultMiddleware<TResponse>> getResponseType(Class<TResponse> responseClass) {
        log.trace("getResponseType started for type : {}", responseClass);
        return new ParameterizedTypeReference<ResultMiddleware<TResponse>>() {
        };
    }
}
