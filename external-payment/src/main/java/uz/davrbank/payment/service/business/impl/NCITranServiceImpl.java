package uz.davrbank.payment.service.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.model.dto.backAPI.request.BackSimpleTransactionRequest;
import uz.davrbank.payment.model.dto.backAPI.response.BackSimpleTranResponse;
import uz.davrbank.payment.model.dto.tran.request.TranBaseRequestDTO;
import uz.davrbank.payment.model.dto.tran.response.TranBaseResponseDTO;
import uz.davrbank.payment.model.enums.DocType;
import uz.davrbank.payment.service.business.NCITranService;
import uz.davrbank.payment.service.web.BackAPIClientService;
import uz.davrbank.payment.utils.PurposeBuilder;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;


@Service
@Slf4j
@RequiredArgsConstructor
public class NCITranServiceImpl implements NCITranService {
    private final BackAPIClientService backAPIClientService;
    private final static String DEFAULT_PURPOSE_CODE = "00634";
    private final static String PARENT_ID = "1001";

    @Override
    public SimpleResponse<TranBaseResponseDTO> credit(TranBaseRequestDTO requestDTO) {
        log.info("Begin credit transaction from request: {}", requestDTO);

        var result = send(requestDTO);

        log.info("Exit credit transaction with result: {}", result);
        return result;
    }

    @Override
    public SimpleResponse<TranBaseResponseDTO> debit(TranBaseRequestDTO requestDTO) {
        log.info("Begin debit transaction from request: {}", requestDTO);

        var result = send(requestDTO);

        log.info("Exit debit transaction with result: {}", result);
        return result;
    }

    private SimpleResponse<TranBaseResponseDTO> send(TranBaseRequestDTO requestDTO) {
        var request = new BackSimpleTransactionRequest(
                requestDTO.getDebitParams().getBranch(),
                requestDTO.getTransaction().getId().toString(),
                requestDTO.getDebitParams().getBranch(),
                requestDTO.getDebitParams().getAccount(),
                "",
                "",
                requestDTO.getCreditParams().getBranch(),
                requestDTO.getCreditParams().getAccount(),
                "",
                "",
                requestDTO.getAmount(),
                PurposeBuilder.defaultBuild(requestDTO.getTransaction()),
                DEFAULT_PURPOSE_CODE,
                DocType.MEMORIAL.getCode(),
                PARENT_ID,
                "3",
                "",
                "",
                requestDTO.getExtId()
        );

        var response = backAPIClientService.simpleTransaction(request);

        if (!response.getStatus()) {
            if (response.isTimeOut())
                return new SimpleResponse<>(response.getError().getMessage(), true);
            else
                return new SimpleResponse<>(response.getError().getCode(), response.getError().getMessage());
        }

        if (response.getData().getRes() == null || !response.getData().getRes().equals("1")) {
            return new SimpleResponse<>(1044, response.getData().getInfo());
        }


        var result = new TranBaseResponseDTO(
                response.getData(),
                request.toMap(),
                response.toMap()
        );
        return new SimpleResponse<>(result);
    }


}
