package uz.devops.humoMiddleware.service;

import uz.devops.humoMiddleware.dto.request.*;
import uz.devops.humoMiddleware.dto.response.*;
import uz.devops.humoMiddleware.dto.basic.*;
import uz.javlon.commons.base.SimpleResultData;

public interface HumoMiddlewareService {

    String NAME = "humoMiddlewareService";

    SimpleResultData<GetRealPanResponseDTO> getRealPan(GetRealPanRequestDTO request);

    SimpleResultData<GetMaskedPanResponseDTO> getMaskedPan(GetMaskedPanRequestDTO request);

    SimpleResultData<GetCardInfoResponseDTO> getCardInfo(GetCardInfoRequestDTO request);

    /**
     * Method activates mobile banking services for the given customer’s card
     * @param requestDTO
     * @return
     */

    SimpleResultData<ResultMiddleware<ClientActivateResponseDTO>> clientActivate(CustomerActivate requestDTO);

    /**
     * Method removes customers card from mobile banking.
     * @param request
     * @return
     */

    SimpleResultData<ResultMiddleware<ClientRemoveCardResponseDTO>> clientRemoveCard(ParamMiddleware<CustomerRemoveCardRequestDTO> request);

    /**
     * Method removes customers card from mobile banking.
     * @param request
     * @return
     */

    SimpleResultData<ResultMiddleware<CustomerEditCardResponseDTO>> clientEditCard(ParamMiddleware<EditCardRequestDTO> request);

    /**
     * Method is used to retrieve basic card and mobile banking information, including information
     * from stop-list
     * @param request
     * @return
     */

    SimpleResultData<ResultMiddleware<RetrieveCardResponseDTO>> retrieveCard(ParamMiddleware<RetrieveCardRequestDTO> request);


    /**
     * Service retrieves card information, card mobile banking information, information of accounts
     * tied to card and balance.
     * @param request
     * @return
     */

    SimpleResultData<ResultMiddleware<RetrieveCardV3ResponseDTO>> retrieveCardV3(ParamMiddleware<RetrieveCardV3RequestDTO> request);

    /**
     * Method is used to retrieve customer cards by passport data
     * @param request
     * @return
     */

    SimpleResultData<ResultMiddleware<GetClientCardsByPassportResponseDTO>> getClientCardsByPassport(ParamMiddleware<GetCardsByPassportRequestDTO> request);

    /**
     * Method is used to retrieve customer cards by Personal code (tax payer code)
     * @param request
     * @return
     */
    SimpleResultData<ResultMiddleware<GetClientCardsByPinflResponseDTO>> getClientCardsByPinfl(ParamMiddleware<GetCardsByPinflRequestDTO> request);

    /* *
     *  (ccr2ExchangeRates)
     *  humo tarafdan aytishlaricha
     *  "Bu service hozir ishlagani yo'q. Hali bankomatlar bilan integratsiya tugagani yo'q. Shuning uchun o'chiq".
     *  Shuning uchun ishlamaydi
     * */
    SimpleResultData<ResultMiddleware<SetExchangeRatesResponseDTO>> setExchangeRate(ParamMiddleware<SetExchangeRateRequestDTO> request);

    /**
     * Method activates mobile banking services for the given customer’s card
     *
     * @param request
     * @return
     */
    SimpleResultData<ResultMiddleware<ClientInfo>> getClientByHumoId(ParamMiddleware<ClientRequestDTO> request);

    /**
     * Method returns customer list by customer phone number
     * @param request
     * @return
     */
    SimpleResultData<ResultMiddleware<GetClientsResponseDTO>> getClientByPhone(ParamMiddleware<CustomerListRequestDTO> request);

    /**
     * Method deactivates mobile banking services for the given customer.
     * @param request
     * @return
     */
    SimpleResultData<ResultMiddleware<ClientDeactivateResponseDTO>> clientDeactivate(ParamMiddleware<CustomerDeactivateRequestDTO> request);

    /**
     * Method returns customerId by customer phone number
     * @param request
     * @return
     */
    SimpleResultData<ResultMiddleware<ChangePhoneNumberResponseDTO>> changeSmsInfoPhoneNumber(ChangePhoneNumber request);

    /**
     * Method changes customers message languages.
     * @param request
     * @return
     */
    SimpleResultData<ResultMiddleware<ChangeSmsInfoLangResponseDTO>> changeSmsInfoLanguage(ParamMiddleware<ChangeSmsInfoLangRequestDTO> request);
}
