package nci.billing.service.simulate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nci.billing.exception.SimulateThrow;
import nci.billing.model.dto.*;
import nci.billing.model.enums.ResponseCode;
import nci.billing.model.response.CheckResponse;
import nci.billing.model.response.BaseIsAPIResponse;
import nci.billing.service.CoreService;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class CoreSimulate implements CoreService {
    private final ObjectMapper objectMapper;
    private final static String path = "resources/static/";

    @Override
    public CheckResponse checkTransaction(CheckPaymentDto request) {
        log.info("Starting simulate checkTransaction....");
        try {
            var responseList = objectMapper.readValue(new File(path + "check.json"), CheckResponse[].class);
            var random = new Random().nextInt(3);
            log.info("random number: {}", random);
            return Arrays.stream(responseList).toList().get(random);
        } catch (Exception ex) {
            log.debug("SIMULATE checkTransaction method read file error: {}", ex.getMessage());
            throw new SimulateThrow("SIMULATE checkTransaction method read file error: " + ex.getMessage());
        }
    }

    @Override
    public ProblemDto pay(PayDto dto) {
        log.info("Starting simulate payTransaction....");
        try {
            var responseList = objectMapper.readValue(new File(path + "paySimulate.json"), ProblemDto[].class);
            var random = new Random().nextInt(2);
            log.info("random number: {}", random);
            return Arrays.stream(responseList).toList().get(random);
        } catch (Exception ex) {
            log.debug("SIMULATE payTransaction method read file error: {}", ex.getMessage());
            throw new SimulateThrow("SIMULATE payTransaction method read file error: " + ex.getMessage());
        }
    }

    @Override
    public ProblemDto reverseNCITransaction(ReverseDto dto) {
        log.info("Starting simulate reverseNCITransaction....");
        try {
            var responseList = objectMapper.readValue(new File(path + "reverse.json"), BaseIsAPIResponse[].class);
            var random = new Random().nextInt(4);
            log.info("random number: {}", random);
            return responseToDto(Arrays.stream(responseList).toList().get(random));
        } catch (Exception ex) {
            log.debug("SIMULATE reverseNCITransaction method read file error: {}", ex.getMessage());
            throw new SimulateThrow("SIMULATE reverseNCITransaction method read file error: " + ex.getMessage());
        }
    }

    @Override
    public ProblemDto repayMainDebtByLoan(RepayMainDebtDto dto) {
        return null;
    }

    @Override
    public ProblemDto repayMainDebtByLoanV2(RepayDTO dto) {
        return null;
    }

    @Override
    public ProblemDto forexOperation(ForexTranRequestDTO requestDTO) {
        return null;
    }

    @Override
    public ProblemDto simpleTransaction(SimpleTranRequestDTO requestDTO) {
        return null;
    }

    private ProblemDto responseToDto(BaseIsAPIResponse response) {
        if (!response.getResult().equals("1"))
            return ProblemDto.builder()
                    .code(ResponseCode.REVERSE_FAILED.getCode())
                    .detail(ResponseCode.REVERSE_FAILED.getMessage())
                    .message(response.getResultNote())
                    .build();
        return ProblemDto.builder()
                .code(ResponseCode.REVERSE_SUCCESS.getCode())
                .detail(ResponseCode.REVERSE_SUCCESS.getMessage())
                .message(response.getResultNote())
                .params(Map.of("doc_id", response.docId))
                .build();
    }
}
