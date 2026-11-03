package uz.davrbank.bustreasury.service.cases.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.davrbank.bustreasury.aggregate.dto.request.user.CheckStatusRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.ResponseDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.ResultDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.user.CheckStatusResponseDTO;
import uz.davrbank.bustreasury.service.CallerService;
import uz.davrbank.bustreasury.service.interfaces.user.CheckStatusInterface;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CheckStatusUserService implements CheckStatusInterface {

    private static final Logger logger = LoggerFactory.getLogger(CheckStatusUserService.class);

    private final CallerService callerService;

    @Autowired
    public CheckStatusUserService(CallerService callerService) {
        this.callerService = callerService;
    }

    @Override
    public List<CheckStatusResponseDTO> getTransactionStatus(CheckStatusRequestDTO request) {
        logger.info("getTransactionStatus chaqirildi: P_TRANS_ID={}, P_GROUP_ID={}", request.getP_TRANS_ID(), request.getP_GROUP_ID());

        ResultDTO gatewayResponse = callerService.SendStatusCheck2Gateway(request, CheckStatusRequestDTO.class);

        if (gatewayResponse.getResponses() == null || gatewayResponse.getResponses().isEmpty()) {
            logger.error("Gateway'dan hech qanday javob olinmadi: P_TRANS_ID={}", request.getP_TRANS_ID());
            CheckStatusResponseDTO errorResponse = new CheckStatusResponseDTO(
                    "NO_RESPONSE",
                    "Gateway'dan javob olinmadi",
                    null,null
            );
            return Collections.singletonList(errorResponse);
        }

        // Javob ro‘yxatini qayta ishlash
        List<CheckStatusResponseDTO> responses = new ArrayList<>();
        for (ResponseDTO responseDTO : gatewayResponse.getResponses()) {
            logger.info("Gateway response ko'rsatilmoqda: ERROR_CODE={}, ERROR_NOTE={}, TRANS_ID={}, STATE_NAME ={}",
                    responseDTO.getErrorCode(), responseDTO.getErrorNote(), responseDTO.getTransId() ,responseDTO.getStateName());

            CheckStatusResponseDTO response = new CheckStatusResponseDTO(
                    responseDTO.getErrorCode(),
                    responseDTO.getErrorNote(),
                    responseDTO.getTransId(),
                    responseDTO.getStateName()

            );

            logger.info("Final response: ERROR_CODE={}, ERROR_NOTE={}, TRANS_ID={}, STATE_NAME={}",
                    response.getERROR_CODE(), response.getERROR_NOTE(), response.getTRANS_ID(), response.getStateName());

            responses.add(response);
        }

        return responses;
    }
}
