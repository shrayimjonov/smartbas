package uz.davrbank.bustreasury.service.cases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.davrbank.bustreasury.aggregate.dto.request.ExchangeRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.request.TreasuryRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.ResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.ResultDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.TemplateResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.WiringResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.Exchange;
import uz.davrbank.bustreasury.aggregate.model.Wiring;
import uz.davrbank.bustreasury.repository.ExchangeRepository;
import uz.davrbank.bustreasury.repository.WiringRepository;
import uz.davrbank.bustreasury.service.CallerService;
import uz.davrbank.bustreasury.service.interfaces.TemplateInterface;
import uz.davrbank.bustreasury.service.interfaces.TreasuryInterface;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TreasuryService implements TreasuryInterface {

    private static final Logger logger = LoggerFactory.getLogger(TreasuryService.class);

    private final WiringRepository wiringRepository;
    private final CallerService callerService;
    private final TemplateInterface templateInterface;
    private final ExchangeRepository exchangeRepository;

    public TreasuryService(WiringRepository wiringRepository, CallerService callerService, TemplateInterface templateInterface, ExchangeRepository exchangeRepository) {
        this.wiringRepository = wiringRepository;
        this.callerService = callerService;
        this.templateInterface = templateInterface;
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    public ResultDTO createTreasury(TreasuryRequestDTO requestDTO) {
        logger.info("createTreasury chaqirildi: clientAccount={}, companyAccount={}", requestDTO.getCreditAccount(), requestDTO.getDebitAccount());
        return callerService.SendDoc2Gateway(requestDTO, TreasuryRequestDTO.class);
    }

    @Override
    public ResultDTO createTreasuryExchange(ExchangeRequestDTO requestDTO) {
        logger.info("createTreasuryExchange chaqirildi: templateId={}, total={}", requestDTO.getTemplateId(), requestDTO.getTotal());

        TemplateResponseDTO templateById = templateInterface.getTemplateById(requestDTO.getTemplateId());
        List<WiringResponseDTO> wiring = templateById.getWiring();
        List<ResponseDTO> responseDTOS = new ArrayList<>();

        for (WiringResponseDTO wiringResponse : wiring) {
            TreasuryRequestDTO treasuryRequestDTO = new TreasuryRequestDTO();
            treasuryRequestDTO.setDebitAccount(wiringResponse.getDebitAccountNum());
            treasuryRequestDTO.setCreditAccount(wiringResponse.getCreditAccountNum());
            treasuryRequestDTO.setCompanyName(wiringResponse.getCreditMFO().getName());
            treasuryRequestDTO.setCompanyBank(wiringResponse.getCreditMFO().getCode());
            treasuryRequestDTO.setCompanyInn(wiringResponse.getCreditInn());
            treasuryRequestDTO.setSum(String.valueOf(requestDTO.getTotal()));
            treasuryRequestDTO.setDate(Date.from(Instant.now()));
            treasuryRequestDTO.setPurposeId(wiringResponse.getPurpose().getCode());
            treasuryRequestDTO.setPurpose(wiringResponse.getPurpose().getPurpose());
            treasuryRequestDTO.setDocumentType(wiringResponse.getDocType().getKodDoc());
            treasuryRequestDTO.setPaymentType(wiringResponse.getTypePlate().getId());
            treasuryRequestDTO.setState(Integer.valueOf(wiringResponse.getPayment().getCode()));
            treasuryRequestDTO.setTypePlate(wiringResponse.getTypePlate().getCode());
            ResultDTO responseDTO = callerService.SendDoc2Gateway(treasuryRequestDTO, TreasuryRequestDTO.class);
            logger.info("Gateway response for wiringId={}: {} responses received", wiringResponse.getWiringId(), responseDTO.getResponses().size());

            if (responseDTO.getResponses() != null && !responseDTO.getResponses().isEmpty()) {
                ResponseDTO firstResponse = responseDTO.getResponses().get(0);
                logger.info("Response details: ERROR_CODE={}, ERROR_NOTE={}, TRANS_ID={}",
                        firstResponse.getErrorCode(), firstResponse.getErrorNote(), firstResponse.getTransId());

                if ("0".equals(firstResponse.getErrorCode())) {

                    Wiring wiringEntity = wiringRepository
                            .findByTemplateIdAndWiringId(requestDTO.getTemplateId(), wiringResponse.getWiringId())
                            .orElseThrow(() -> new IllegalStateException(
                                    "Wiring topilmadi: templateId=" + requestDTO.getTemplateId() +
                                            ", wiringId=" + wiringResponse.getWiringId()));

                    Exchange exchange = new Exchange();
                    exchange.setTotal(requestDTO.getTotal());
                    exchange.setCreatedBy(requestDTO.getWorker());
                    exchange.setWiring(wiringEntity);
                    exchangeRepository.save(exchange);
                    logger.info("Exchange saved for wiringId={}: total={}, createdBy={}",
                            wiringResponse.getWiringId(), requestDTO.getTotal(), requestDTO.getWorker());
                }
                responseDTOS.add(firstResponse);
            } else {
                logger.warn("No responses received from gateway for wiringId={}", wiringResponse.getWiringId());
                ResponseDTO errorResponse = new ResponseDTO();
                errorResponse.setErrorCode("NO_RESPONSE");
                errorResponse.setErrorNote("Gateway'dan javob olinmadi");
                errorResponse.setTransId(null);
                errorResponse.setGroupId(String.valueOf(0L));
                responseDTOS.add(errorResponse);
            }
        }

        return new ResultDTO(responseDTOS);
    }
}
