package uz.devops.humoMiddleware.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uz.devops.humoMiddleware.config.TestConfig;
import uz.devops.humoMiddleware.dto.basic.*;
import uz.devops.humoMiddleware.dto.request.*;
import uz.devops.humoMiddleware.dto.response.*;
import uz.devops.humoMiddleware.service.HumoMiddlewareService;
import uz.javlon.commons.base.SimpleResultData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class, HumoMiddlewareService.class, HumoMiddlewareServiceImpl.class})
public class HumoMiddlewareServiceMImplTest {

    @Autowired
    @Qualifier(value = "humoMiddlewareService")
    public HumoMiddlewareService humoMiddlewareService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetRealPan() {
        GetRealPanRequestDTO requestDTO = new GetRealPanRequestDTO();
        requestDTO.setMaskedPan("986009PLVFPY1907");
        SimpleResultData<GetRealPanResponseDTO> response = humoMiddlewareService.getRealPan(requestDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    public void testGetMaskedPan() {
        GetMaskedPanRequestDTO requestDTO = new GetMaskedPanRequestDTO();
        requestDTO.setPan("98600901014319071");
        SimpleResultData<GetMaskedPanResponseDTO> response = humoMiddlewareService.getMaskedPan(requestDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    public void testGetCardInfo() {
        GetCardInfoRequestDTO requestDTO = new GetCardInfoRequestDTO();
        requestDTO.setCard("9860090000000000");
        requestDTO.setExpiry("1225");
        SimpleResultData<GetCardInfoResponseDTO> response = humoMiddlewareService.getCardInfo(requestDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    //METHOD DOES NOT WORK
    @Test
    public void testCcr2ExchangeRates() {
        RateInfo dto = new RateInfo();
        ParamMiddleware<SetExchangeRateRequestDTO> paramMiddleware = new ParamMiddleware<>();
        SetExchangeRateRequestDTO requestDTO = new SetExchangeRateRequestDTO();
        dto.setOwner("09");
        dto.setRateSet("AAB-TES2-860");
        dto.setDirectQuotation("1");
        dto.setBaseCurrency("860");
        dto.setCounterCurrency("840");
        dto.setEffectiveDate("2021-07-04");
        dto.setSellRate("10600");
        dto.setBuyRate("10500");
        List<RateInfo> a = new ArrayList<>();
        a.add(dto);
        requestDTO.setRate(a);
        paramMiddleware.setId("qweqweqwe212");
        paramMiddleware.setParams(requestDTO);
        SimpleResultData<ResultMiddleware<SetExchangeRatesResponseDTO>> response = humoMiddlewareService.setExchangeRate(paramMiddleware);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    public void testCustomerActivate() {
        CustomerActivate requestDTO = new CustomerActivate();
        Charge charge = new Charge();
        charge.setAgreementCharge("MONTH.FEE.OFF");
        charge.setChargeAccount("-");

        Service service = new Service();
        service.setServiceId("MB-ALL");
        service.setServiceChannel("-");

        Card card = new Card();
        card.setPan("9860090000000000");
        card.setLabel("HUMO-CARD");
        card.setService(Collections.singletonList(service));
        card.setCharge(charge);

        Phone phone = new Phone();
        phone.setPhoneNumber("+998901234567");

        Email email = new Email();
        email.setTo("user@example.com");

        ClientInfo request = new ClientInfo();
        request.setBankId("MB-STD");
        request.setLanguage(Lang.RU);
        request.setCharge(charge);
        request.setCard(card);
        request.setPhone(phone);
        request.setEmail(email);

        requestDTO.setId("123456789");
        requestDTO.setParams(request);
        SimpleResultData<ResultMiddleware<uz.devops.humoMiddleware.dto.response.ClientActivateResponseDTO>> response = humoMiddlewareService.clientActivate(requestDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    //SUCCESS
    @Test
    public void testCustomer() {
        ParamMiddleware<ClientRequestDTO> requestDTO = new ParamMiddleware<>();
        ClientRequestDTO request = new ClientRequestDTO();
        request.setCustomerId("00004449-09");
        request.setBankId("MB_STD");
        requestDTO.setId("123456789");
        requestDTO.setParams(request);
        SimpleResultData<ResultMiddleware<ClientInfo>> response = humoMiddlewareService.getClientByHumoId(requestDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    //SUCCESS
    @Test
    public void testCustomerList() {
        ParamMiddleware<CustomerListRequestDTO> requestDTO = new ParamMiddleware<>();
        CustomerListRequestDTO request = new CustomerListRequestDTO();
        request.setPhone("+998901234567");
        request.setBankId("MB_STD");
        requestDTO.setId("123456789");
        requestDTO.setParams(request);
        SimpleResultData<ResultMiddleware<GetClientsResponseDTO>> response = humoMiddlewareService.getClientByPhone(requestDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    //SUCCESS
    @Test
    public void testCustomerDeactivate() {
        ParamMiddleware<CustomerDeactivateRequestDTO> requestDTO = new ParamMiddleware<>();
        CustomerDeactivateRequestDTO request = new CustomerDeactivateRequestDTO();
        request.setCustomerId("00004190-09");
        requestDTO.setId("123456789");
        requestDTO.setParams(request);
        SimpleResultData<ResultMiddleware<ClientDeactivateResponseDTO>> response = humoMiddlewareService.clientDeactivate(requestDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    //SUCCESS
    @Test
    public void testChangePhoneNumber() {
        ChangePhoneNumber requestDTO = new ChangePhoneNumber();

        Phone phone = new Phone();
        phone.setPhoneNumber("+998901234567");
        phone.setDeliveryChannel("-");

        ChangeSmsInfoNumberRequestDTO request = new ChangeSmsInfoNumberRequestDTO();
        request.setCustomerId("0000427386-09");
        request.setPhone(phone);

        requestDTO.setId("123456");
        requestDTO.setParams(request);

        SimpleResultData<ResultMiddleware<ChangePhoneNumberResponseDTO>> response = humoMiddlewareService.changeSmsInfoPhoneNumber(requestDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    //SUCCESS
    @Test
    public void testChangeMessageLang() {
        ParamMiddleware<ChangeSmsInfoLangRequestDTO> requestDTO = new ParamMiddleware<>();
        ChangeSmsInfoLangRequestDTO request = new ChangeSmsInfoLangRequestDTO();
        request.setCustomerId("0000427386-09");
        request.setLanguage(Lang.EN);
//        request.setLanguage(Language.EN);

        requestDTO.setId("123456");
        requestDTO.setParams(request);
        SimpleResultData<ResultMiddleware<ChangeSmsInfoLangResponseDTO>> response = humoMiddlewareService.changeSmsInfoLanguage(requestDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }


    //SUCCESS
    @Test
    public void testCustomerCardsByPersonCode() {
        GetCardsByPinflRequestDTO dto = new GetCardsByPinflRequestDTO();
        dto.setPinfl("1234567890123456");
        ParamMiddleware<GetCardsByPinflRequestDTO> requestDTO = new ParamMiddleware<>();
        requestDTO.setParams(dto);
        requestDTO.setId("aqaqqaaqq");
        SimpleResultData<ResultMiddleware<GetClientCardsByPinflResponseDTO>> response = humoMiddlewareService.getClientCardsByPinfl(requestDTO);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    //SUCCESS
    @Test
    public void testCustomerCardsByPassport() {
        ParamMiddleware<GetCardsByPassportRequestDTO> dto = new ParamMiddleware<>();

        GetCardsByPassportRequestDTO cards = new GetCardsByPassportRequestDTO();

        cards.setSerial("AA");
        cards.setNumber("123456789");
        dto.setParams(cards);
        dto.setId("123123");

        SimpleResultData<ResultMiddleware<GetClientCardsByPassportResponseDTO>> response = humoMiddlewareService.getClientCardsByPassport(dto);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    //SUCCESS
    @Test
    public void testIiaCsCard() {
        ParamMiddleware<RetrieveCardRequestDTO> dto = new ParamMiddleware<>();

        RetrieveCardRequestDTO cards = new RetrieveCardRequestDTO();

        cards.setFlag(1);
        cards.setPrimaryAccountNumber("9860090000000000");
        dto.setParams(cards);
        dto.setId("123456789");

        SimpleResultData<ResultMiddleware<RetrieveCardResponseDTO>> response = humoMiddlewareService.retrieveCard(dto);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    //SUCCESS
    @Test
    public void testCustomerEditCard() {
        ParamMiddleware<EditCardRequestDTO> dto = new ParamMiddleware<>();

        EditCardRequestDTO cards = new EditCardRequestDTO();
        Card card = new Card();
        card.setPan("9860090000000000");
        card.setLabel("HUMO-CARD");
        card.setService(Arrays.asList(new Service("MB-ALL", "-"), new Service("MB-ALL", "-")));
        card.setCharge(new Charge("MONTH.FEE.OFF", "-"));
        cards.setCard(card);

        dto.setParams(cards);
        dto.setId("123321");
        SimpleResultData<ResultMiddleware<CustomerEditCardResponseDTO>> response = humoMiddlewareService.clientEditCard(dto);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    //SUCCESS
    @Test
    public void testCustomerRemoveCard() {
        ParamMiddleware<CustomerRemoveCardRequestDTO> dto = new ParamMiddleware<>();

        CustomerRemoveCardRequestDTO cards = new CustomerRemoveCardRequestDTO();
        cards.setCard(new PanInfo("9860090000000000"));
        dto.setParams(cards);
        dto.setId("123456789");

        SimpleResultData<ResultMiddleware<ClientRemoveCardResponseDTO>> response = humoMiddlewareService.clientRemoveCard(dto);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    public void testIiaCsCardV3() throws JsonProcessingException {
        ParamMiddleware<RetrieveCardV3RequestDTO> retrieveCardV3RequestDTOParam = new ParamMiddleware<>();

        RetrieveCardV3RequestDTO requestDTO = new RetrieveCardV3RequestDTO();

        requestDTO.setFlag(1);
        requestDTO.setPrimaryAccountNumber("9860090000000000");
        retrieveCardV3RequestDTOParam.setParams(requestDTO);
        retrieveCardV3RequestDTOParam.setId("123456789");

        System.out.println("REQUEST: " + '\n' + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(retrieveCardV3RequestDTOParam) + '\n');

        var middlewareSimpleResultData = humoMiddlewareService.retrieveCardV3(retrieveCardV3RequestDTOParam);

        System.out.println("RESPONSE: " + '\n' + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(middlewareSimpleResultData.getData().getResult()) + '\n');
        assertThat(middlewareSimpleResultData).isNotNull();
        assertThat(middlewareSimpleResultData.getData()).isNotNull();
        assertThat(middlewareSimpleResultData.getData().getResult()).isNotNull();
        assertThat(middlewareSimpleResultData.getData().getResult().getAccount().get(0).getInstitutionId()).isEqualTo("AsiaAllianc");
        assertThat(middlewareSimpleResultData.getData().getResult().getBalance().getCurrency()).isEqualTo("860");
        assertThat(middlewareSimpleResultData.isSuccess()).isTrue();
    }

    @SneakyThrows
    @Test
    public void testDeserialize() {

        String fromHumoResponse = "{\n" +
            "        \"listSize\": 1,\n" +
            "        \"card\": {\n" +
            "            \"institutionId\": \"AnorBank\",\n" +
            "            \"primaryAccountNumber\": \"9860600000000000\",\n" +
            "            \"effectiveDate\": \"2022-04-12T06:33:08.000Z\",\n" +
            "            \"updateDate\": \"2022-04-12T06:35:58.000Z\",\n" +
            "            \"prefixNumber\": 5,\n" +
            "            \"expiry\": \"2704\",\n" +
            "            \"cardSequenceNumber\": 1,\n" +
            "            \"cardholderId\": \"00051476\",\n" +
            "            \"nameOnCard\": \"YALLAYEV ASLOM\",\n" +
            "            \"accountRestrictionsFlag\": \"E\",\n" +
            "            \"commissionGroup\": \"01001\",\n" +
            "            \"cardUserId\": \"00051476\",\n" +
            "            \"additionalInfo\": \"CBS0011AGN00818265832\",\n" +
            "            \"riskGroup\": \"A\",\n" +
            "            \"riskGroup2\": \"A\",\n" +
            "            \"bankC\": \"60\",\n" +
            "            \"pinTryCount\": 0,\n" +
            "            \"statuses\": {\n" +
            "                \"item\": [\n" +
            "                    {\n" +
            "                        \"type\": \"card\",\n" +
            "                        \"actionCode\": \"000\",\n" +
            "                        \"actionDescription\": \"Approved\",\n" +
            "                        \"effectiveDate\": \"2022-04-12T06:33:08.000Z\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"type\": \"card2\",\n" +
            "                        \"actionCode\": \"118\",\n" +
            "                        \"actionDescription\": \"Decline, no card record\",\n" +
            "                        \"effectiveDate\": \"2022-04-12T06:33:08.000Z\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"type\": \"hot\",\n" +
            "                        \"actionCode\": \"000\",\n" +
            "                        \"actionDescription\": \"Approved\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"type\": \"issuer\",\n" +
            "                        \"actionCode\": \"000\",\n" +
            "                        \"actionDescription\": \"Approved\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"type\": \"user\",\n" +
            "                        \"actionCode\": \"000\",\n" +
            "                        \"actionDescription\": \"Approved\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        },\n" +
            "        \"account\": [\n" +
            "            {\n" +
            "                \"institutionId\": \"AnorBank\",\n" +
            "                \"accountId\": \"7473932\",\n" +
            "                \"bankAccountId\": \"6001183000063481\",\n" +
            "                \"currency\": \"860\",\n" +
            "                \"cardholderId\": \"00051476\",\n" +
            "                \"effectiveDate\": \"2022-06-13T23:02:48.000Z\",\n" +
            "                \"updateDate\": \"2022-06-13T23:33:00.000Z\",\n" +
            "                \"accountType\": \"Offline chip\",\n" +
            "                \"initialAmount\": 61270526,\n" +
            "                \"lockedBackofficeAmount\": 59160500,\n" +
            "                \"lockTime\": \"2022-06-13T22:57:29.000Z\",\n" +
            "                \"lockedAmount\": 300000,\n" +
            "                \"shadowAmount\": 0,\n" +
            "                \"commissionGroup\": \"01001\",\n" +
            "                \"priority\": 1,\n" +
            "                \"status\": \"Active\",\n" +
            "                \"availableAmount\": 1810026\n" +
            "            }\n" +
            "        ],\n" +
            "        \"balance\": {\n" +
            "            \"currency\": \"860\",\n" +
            "            \"initialAmount\": 61270526,\n" +
            "            \"bonusAmount\": 0,\n" +
            "            \"creditLimit\": 0,\n" +
            "            \"lockedBackofficeAmount\": 59160500,\n" +
            "            \"lockedBackofficeAmountOffline\": 0,\n" +
            "            \"lockedAmount\": 300000,\n" +
            "            \"availableAmount\": 1810026\n" +
            "        },\n" +
            "        \"mb\": {\n" +
            "            \"state\": \"on\",\n" +
            "            \"phone\": \"+998901234567\",\n" +
            "            \"message\": \"Status of the customer mobile agreement is active\"\n" +
            "        }\n" +
            "    }";

        final RetrieveCardV3ResponseDTO retrieveCardV3ResponseDTO = objectMapper.readValue(fromHumoResponse, RetrieveCardV3ResponseDTO.class);

        System.out.println(retrieveCardV3ResponseDTO);
    }
}
