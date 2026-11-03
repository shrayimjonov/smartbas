package nci.billing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nci.billing.exception.TimeOut;
import nci.billing.feign.NciGatewayFeign;
import nci.billing.model.dto.CardsByPersonCodeResponseDTO;
import nci.billing.model.dto.DebtsDto;
import nci.billing.model.dto.LoanDto;
import nci.billing.model.dto.response.ResultData;
import nci.billing.model.enums.ResponseCode;
import nci.billing.model.request.*;
import nci.billing.model.response.*;
import nci.billing.model.response.nci.NCIClientInfoResponse;
import nci.billing.service.InfoV2Service;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfoV2Impl implements InfoV2Service {
    private final NciGatewayFeign nciGatewayFeign;

    @Override
    public ResultData<LoanResponse> getLoanInfoV2(LoanDto dto) {
        try {
            return nciGatewayFeign.getLoanInfoByLoanExtId(new LoanInfoRequest(dto.getLoanId()));
        } catch (Exception e) {
            return new ResultData<>(ResponseCode.FAILED, e.getMessage());
        }
    }

    @Override
    public ResultData<ClientResponse> getClientInfo(LoanDto dto) {
        try {
            var response = nciGatewayFeign.getClientDataByLoanId(new LoanRequest(dto.getLoanId()));
            if (response.getError() != null)
                return new ResultData<>(ResponseCode.FAILED, response.getError().getMessage());
            else
                return new ResultData<>(response);
        } catch (Exception e) {
            return new ResultData<>(ResponseCode.FAILED, e.getMessage());
        }
    }

    @Override
    public ResultData<DebtsListResponse> getDebtsByBranch(DebtsDto dto) {
        try {
            var response = nciGatewayFeign.getDebtsListByBranch(new DebtsRequest(dto.getBranch()));
            if (response.getError() != null)
                return new ResultData<>(ResponseCode.FAILED, response.getError().getMessage());
            else
                return new ResultData<>(response);
        } catch (Exception e) {
            return new ResultData<>(ResponseCode.FAILED, e.getMessage());
        }
    }

    @Override
    public ResultData<List<TransactionResponse>> getLoanTransactions(TransactionRequest request) {
        try {
            var response = nciGatewayFeign.getLoanTransactions(request);
            return new ResultData<>(Arrays.stream(response).toList());
        } catch (Exception e) {
            return new ResultData<>(ResponseCode.FAILED, e.getMessage());
        }
    }

    @Override
    public ResultData<List<CourseResponse>> getCourse() {
        try {
            return nciGatewayFeign.getCourse();
        } catch (Exception ex) {
            throw new TimeOut("nciGateway getCourse method throws error: %s".formatted(ex.getMessage()));
        }
    }


    @Override
    public ResultData<List<CardsByPersonCodeResponseDTO>> getCardsByPersonCode(String personCode) {
        try {
            var response = nciGatewayFeign.getCardsByPersonCode(personCode);

            if (response.getError() != null)
                return new ResultData<>(-214, response.getError().getMessage());

            return new ResultData<>(response
                    .getResult()
                    .stream()
                    .map(CardsByPersonCodeResponseDTO::new)
                    .toList());

        } catch (Exception ex) {
            throw new TimeOut("nciGateway getCardsByPersonCode method throws error: %s".formatted(ex.getMessage()));
        }
    }

    @Override
    public ResultData<List<NCIClientInfoResponse>> getClientInfoByPINFL(String personCode) {
        log.info("Sending request to nci_gateway for getClientInfoByPINFL method by personCode: {}", personCode);
        try {

            var request = new NCIClientInfoByPINFLRequest(
                    personCode
            );

            var response = nciGatewayFeign.getClientInfoByPINFL(request);

            return new ResultData<>(response);

        } catch (Exception ex) {
            throw new TimeOut("nciGateway getClientInfoByPINFL method throws error: %s".formatted(ex.getMessage()));
        }
    }

}
