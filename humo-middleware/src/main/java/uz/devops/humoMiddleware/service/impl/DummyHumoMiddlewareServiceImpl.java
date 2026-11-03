package uz.devops.humoMiddleware.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import uz.devops.humoMiddleware.dto.basic.*;
import uz.devops.humoMiddleware.dto.enums.AccountStatus;
import uz.devops.humoMiddleware.dto.enums.AccountType;
import uz.devops.humoMiddleware.dto.request.*;
import uz.devops.humoMiddleware.dto.response.*;
import uz.devops.humoMiddleware.service.HumoMiddlewareService;
import uz.javlon.commons.base.SimpleResultData;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


@Slf4j
@Service(HumoMiddlewareService.NAME)
@ConditionalOnProperty(
    prefix = "humo-middleware",
    name = "simulate",
    havingValue = "true",
    matchIfMissing = true
)
public class DummyHumoMiddlewareServiceImpl implements HumoMiddlewareService {


    public static final String TEST_PAN = "9860090000000000";
    public static final String MASKED_PAN = "986009PLVFPY1907";
    public static final String TIMEOUT_MASKED_PAN = "986060PLVFPY0000";
    public static final String TIMEOUT_REAL_PAN = "9860600000000000";

    @Autowired
    public DummyHumoMiddlewareServiceImpl() {
        log.debug("################ Humo middleware simulation is ON ###############");
    }

    @Override
    public SimpleResultData<GetRealPanResponseDTO> getRealPan(GetRealPanRequestDTO request) {
        if (Objects.equals(request.getMaskedPan(), TIMEOUT_MASKED_PAN)) {
            return new SimpleResultData<>(new GetRealPanResponseDTO(TIMEOUT_REAL_PAN));
        }
        return new SimpleResultData<>(new GetRealPanResponseDTO(TEST_PAN));
    }

    @Override
    public SimpleResultData<GetMaskedPanResponseDTO> getMaskedPan(GetMaskedPanRequestDTO request) {
        return new SimpleResultData<>(new GetMaskedPanResponseDTO(MASKED_PAN));
    }

    @Override
    public SimpleResultData<GetCardInfoResponseDTO> getCardInfo(GetCardInfoRequestDTO request) {
        CardDataInfo cardDataInfo = new CardDataInfo();

        CardInfo cardInfo = new CardInfo();
        cardInfo.setPan(request.getCard());
        cardInfo.setExpiry(request.getExpiry());
        cardInfo.setNameOnCard("CARD/HOLDER-NAME");
        cardInfo.setCardholderId(String.valueOf(System.currentTimeMillis()));
        cardInfo.setPinTryCount("0");
        StatusInfo statusInfo = new StatusInfo("000", "active", "2021-11-18T20:26:26");
        cardInfo.setStatuses(statusInfo);
        cardDataInfo.setCard(cardInfo);


        AgreementInfo agreementInfo = new AgreementInfo("active", "on", "+998901234567");
        cardDataInfo.setAgreement(agreementInfo);

        RecordInfo info = new RecordInfo();
        info.setData(Collections.singletonList(cardDataInfo));

        return new SimpleResultData(new GetCardInfoResponseDTO(2, 2, 0, Collections.singletonList(info)));
    }

    @Override
    public SimpleResultData<ResultMiddleware<ClientActivateResponseDTO>> clientActivate(CustomerActivate request) {
        return new SimpleResultData<>(new ResultMiddleware<>("123456789", new ClientActivateResponseDTO("on", "Mobile banking service successfully activated for the customer")));
    }

    public SimpleResultData<ResultMiddleware<ClientInfo>> getClientByHumoId(ParamMiddleware<ClientRequestDTO> request) {
        Charge charge = new Charge();
        charge.setChargeAccount("MONTH.FEE.OFF");
        charge.setChargeAccount("-");

        uz.devops.humoMiddleware.dto.basic.Service service = new uz.devops.humoMiddleware.dto.basic.Service();
        service.setServiceId("MB-ALL");
        service.setServiceChannel("-");

        Card card = new Card();
        card.setPan("986009******6138");
        card.setLabel("HUMO-CARD");
        card.setService(Collections.singletonList(service));
        card.setCharge(charge);


        Phone phone = new Phone();
        //phone.setState("on");
        phone.setPhoneNumber("+9989********");

        Email email = new Email();
        email.setTo("user@example.com");

        ClientInfo responseDTO = new ClientInfo();
//        responseDTO.setCustomerId("00004449-09");
        responseDTO.setBankId("MB_STD");
//        responseDTO.setCardholderName("John Doe");
//        responseDTO.setState("on");
//        responseDTO.setLanguage("ru_translist");
        responseDTO.setCharge(charge);
        responseDTO.setCard((card));
        responseDTO.setPhone((phone));
        responseDTO.setEmail((email));

        return new SimpleResultData<>(new ResultMiddleware<>("123456789", responseDTO));
    }

    public SimpleResultData<ResultMiddleware<GetClientsResponseDTO>> getClientByPhone(ParamMiddleware<CustomerListRequestDTO> request) {
        uz.devops.humoMiddleware.dto.basic.Service service = new uz.devops.humoMiddleware.dto.basic.Service();
        service.setServiceId("MB-ALL");
        service.setServiceChannel("-");

        Charge charge = new Charge();
        charge.setAgreementCharge("MONTH.FEE.OFF");
        charge.setChargeAccount("-");

        Card card = new Card();
        card.setState("on");
        card.setPan("986009******6138");
        card.setExpiry("2612");
        card.setLabel("HUMO-CARD");
        card.setService(Collections.singletonList(service));
        card.setCharge(charge);

        Customer customer = new Customer();
        customer.setCustomerId("00004190-09");
        customer.setBankId("MB_STD");
        customer.setCard(Collections.singletonList(card));

        GetClientsResponseDTO responseDTO = new GetClientsResponseDTO();
        responseDTO.setCustomer(Collections.singletonList(customer));

        return new SimpleResultData<>(new ResultMiddleware<>("123456789", responseDTO));
    }

    public SimpleResultData<ResultMiddleware<ClientDeactivateResponseDTO>> clientDeactivate(ParamMiddleware<CustomerDeactivateRequestDTO> request) {
        return new SimpleResultData<>(new ResultMiddleware<>("123456789", new ClientDeactivateResponseDTO("off", "Mobile Banking service successfully deactivated for the customer")));
    }

    public SimpleResultData<ResultMiddleware<ChangePhoneNumberResponseDTO>> changeSmsInfoPhoneNumber(ChangePhoneNumber request) {
        return new SimpleResultData<>(new ResultMiddleware<>("123456", new ChangePhoneNumberResponseDTO("on", "Customer phone number successfully changed")));
    }

    public SimpleResultData<ResultMiddleware<ChangeSmsInfoLangResponseDTO>> changeSmsInfoLanguage(ParamMiddleware<ChangeSmsInfoLangRequestDTO> request) {
        return new SimpleResultData<>(new ResultMiddleware<>("123456", new ChangeSmsInfoLangResponseDTO("on", "Customer language successfully changed")));
    }

    @Override
    public SimpleResultData<ResultMiddleware<ClientRemoveCardResponseDTO>> clientRemoveCard(ParamMiddleware<CustomerRemoveCardRequestDTO> request) {
        return new SimpleResultData<>(new ResultMiddleware<>("1233333333333333333", new ClientRemoveCardResponseDTO("off", "Customer card successfully removed")));
    }

    @Override
    public SimpleResultData<ResultMiddleware<CustomerEditCardResponseDTO>> clientEditCard(ParamMiddleware<EditCardRequestDTO> request) {
        return new SimpleResultData<>(new ResultMiddleware<>("1233333333333333333", new CustomerEditCardResponseDTO("on", "Customer card successfully edited")));
    }

    @Override
    public SimpleResultData<ResultMiddleware<RetrieveCardResponseDTO>> retrieveCard(ParamMiddleware<RetrieveCardRequestDTO> request) {
        RetrieveCardResponseDTO dto = new RetrieveCardResponseDTO();
        CardRecord cardRecord = new CardRecord();
        CardStatus cardStatus = new CardStatus();
        cardStatus.setItem(Collections.singletonList(new ItemInfo("card", "125", "Decline, card not effective", "2021-08-30T07:11:51.000Z")));
        cardRecord.setStatuses(cardStatus);
        cardRecord.setInstitutionId("AsiaAllianc");
        cardRecord.setPrimaryAccountNumber("986009******6138");
        cardRecord.setEffectiveDate("2021-08-30T07:11:51.000Z");
        cardRecord.setUpdateDate("2021-08-30T07:16:04.000Z");
        cardRecord.setPrefixNumber("2");
        cardRecord.setExpiry("2608");
        MobileBank mobileBank = new MobileBank();
        mobileBank.setMessage("Status of the customer mobile agreement is active");
        mobileBank.setPhone("+99899*****96");
        mobileBank.setState("on");
        dto.setMb(mobileBank);
        dto.setCard(cardRecord);
        return new SimpleResultData<>(new ResultMiddleware<>("1233333333333333333", dto));
    }

    @Override
    public SimpleResultData<ResultMiddleware<RetrieveCardV3ResponseDTO>> retrieveCardV3(ParamMiddleware<RetrieveCardV3RequestDTO> request) {
        RetrieveCardV3ResponseDTO cardV3ResponseDTO = new RetrieveCardV3ResponseDTO();

        CardRecord cardRecord = new CardRecord();
        cardRecord.setInstitutionId("AsiaAllianc");
        cardRecord.setPrimaryAccountNumber("986009******6138");
        cardRecord.setEffectiveDate("2020-03-03T10:44:32Z");
        cardRecord.setUpdateDate("2020-03-03T10:45:33Z");
        cardRecord.setPrefixNumber("2");
        cardRecord.setExpiry("2502");
        cardRecord.setPinTryCount("0");
        cardRecord.setCardSequenceNumber("001");
        cardRecord.setCardholderId("00000547");
        cardRecord.setNameOnCard("JOHN DOE");
        cardRecord.setCardUserId("00000547");
        cardRecord.setBankC("09");
        cardRecord.setPinTryCount("0");
        cardRecord.setRiskGroup("A");
        cardRecord.setRiskGroup2("A");
        cardRecord.setCommissionGroup("01002");
        cardRecord.setAccountRestrictionsFlag("E");
        cardRecord.setCardholderPassword("JOHN");
        cardRecord.setCardSequenceNumber("1");
        cardRecord.setAdditionalInfo("BRD00820000720IDN002ABCBS0011");


        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setInstitutionId("AsiaAllianc");
        accountRecord.setAccountId("5734");
        accountRecord.setStatus(AccountStatus.Active);
        accountRecord.setCurrency("860");
        accountRecord.setBankAccountId("0901095000002368");
        accountRecord.setCardholderId("00002113");
        accountRecord.setAccountType(AccountType.DEFAULT);
        accountRecord.setLockTime("2021-11-19T10:46:52.000Z");
        accountRecord.setInitialAmount(200400010L);
        accountRecord.setLockedBackofficeAmount(-260000L);
        accountRecord.setLockedAmount(150L);
        accountRecord.setShadowAmount(0L);
        accountRecord.setPriority(1L);
        accountRecord.setAvailableAmount(200660010L);
        accountRecord.setExponent(2);

        BalanceRecord balanceRecord = new BalanceRecord();
        balanceRecord.setCurrency("860");
        balanceRecord.setInitialAmount(200400010L);
        balanceRecord.setBonusAmount(0L);
        balanceRecord.setCreditLimit(0L);
        balanceRecord.setAvailableAmount(1000000000L);
        balanceRecord.setLockedAmount(0L);
        balanceRecord.setLockedBackofficeAmount(-260000L);
        balanceRecord.setLockedBackofficeAmountOffline(0L);
        balanceRecord.setExponent(2L);

        MobileBank mobileBank = new MobileBank();
        mobileBank.setState("on");
        mobileBank.setPhone("979559770");
        mobileBank.setMessage("Status of the customer mobile agreement is active");

        RetrieveCardV3RequestDTO cardV3Request = request.getParams();
        if (Objects.equals(cardV3Request.getPrimaryAccountNumber(), "9860990000000000") ||
            Objects.equals(cardV3Request.getPrimaryAccountNumber(), "9860090000000000")) {

            cardRecord.setPrimaryAccountNumber("9860090000000000");
            cardRecord.setEffectiveDate("2020-03-03T10:44:32Z");
            cardRecord.setUpdateDate("2020-03-03T10:45:33Z");
            cardRecord.setNameOnCard("TEST_F6 TEST_S6");
            cardRecord.setAdditionalInfo("CBS0011");

            accountRecord.setAvailableAmount(0L);
            accountRecord.setInitialAmount(0L);
            accountRecord.setAccountId("3759");
            accountRecord.setBankAccountId("0901095000000428");
            accountRecord.setCardholderId("00000547");
            accountRecord.setEffectiveDate("2020-03-03T15:44:49Z");
            accountRecord.setUpdateDate("2020-03-03T15:50:22Z");
            accountRecord.setBonusAmountExpiry("2020-03-03T15:44:32Z");
            accountRecord.setLockedBackofficeAmount(0L);

            balanceRecord.setAvailableAmount(0L);
            balanceRecord.setInitialAmount(0L);
            balanceRecord.setLockedBackofficeAmount(0L);
        }

        cardV3ResponseDTO.setListSize(1L);
        cardV3ResponseDTO.setCard(cardRecord);
        cardV3ResponseDTO.setAccount(Collections.singletonList(accountRecord));
        cardV3ResponseDTO.setBalance(balanceRecord);
        cardV3ResponseDTO.setMb(mobileBank);

        return new SimpleResultData<>(new ResultMiddleware<>("1ka4s5d4sa5dhhf5", cardV3ResponseDTO));

    }

    @Override
    public SimpleResultData<ResultMiddleware<GetClientCardsByPassportResponseDTO>> getClientCardsByPassport(ParamMiddleware<GetCardsByPassportRequestDTO> request) {
        return new SimpleResultData<>(new ResultMiddleware<>("1233333333333333333", new GetClientCardsByPassportResponseDTO(Arrays.asList("986009MOUJWT2341", "986009YVSNEU3992"))));
    }

    @Override
    public SimpleResultData<ResultMiddleware<GetClientCardsByPinflResponseDTO>> getClientCardsByPinfl(ParamMiddleware<GetCardsByPinflRequestDTO> request) {
        return new SimpleResultData<>(new ResultMiddleware<>("1233333333333333333", new GetClientCardsByPinflResponseDTO(Arrays.asList("986009MOUJWT2341", "986009YVSNEU3992"))));
    }

    @Override
    public SimpleResultData<ResultMiddleware<SetExchangeRatesResponseDTO>> setExchangeRate(ParamMiddleware<SetExchangeRateRequestDTO> request) {
        return new SimpleResultData<>(new ResultMiddleware<>("1233333333333333333", new SetExchangeRatesResponseDTO("Rates updated successfully")));
    }
}
