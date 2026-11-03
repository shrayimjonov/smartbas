package humo.integration.card.Service.v2.impl;

import humo.integration.card.Exception.GatewayException;
import humo.integration.card.Feign.HumoFeign.HumoCardFeign;
import humo.integration.card.Model.Dto.core.request.CardConfirmRequestDTO;
import humo.integration.card.Model.Dto.core.request.CardVerifyRequestDTO;
import humo.integration.card.Model.Dto.core.response.ResultData;
import humo.integration.card.Model.Request.v2.HuMoCardBaseRequest;
import humo.integration.card.Model.Request.v2.params.HuMoCardConfirmParams;
import humo.integration.card.Model.Request.v2.params.HuMoCardVerifyParams;
import humo.integration.card.Model.Response.v2.HuMoCardVerifyBaseResponse;
import humo.integration.card.Model.Response.v2.result.HuMoCardConfirmResult;
import humo.integration.card.Model.Response.v2.result.HuMoCardVerifyResult;
import humo.integration.card.Service.v2.CardVerifyService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardVerifyServiceImpl implements CardVerifyService {
    private final HumoCardFeign client;

    @Override
    public ResultData<HuMoCardVerifyBaseResponse<HuMoCardVerifyResult>> verify(@NonNull CardVerifyRequestDTO requestDTO) {
        log.info("HuMo card verify request: {}", requestDTO);
        HuMoCardBaseRequest<HuMoCardVerifyParams> request = new HuMoCardBaseRequest<>(
                "humo_integration_" + System.currentTimeMillis(),
                new HuMoCardVerifyParams(
                        requestDTO.getPan(),
                        requestDTO.getPhone()
                )
        );

        try {
            HuMoCardVerifyBaseResponse<HuMoCardVerifyResult> response = client.verify(request);
            if (response.getError() != null) {
                log.error(String.format("HuMo verify method throws error: %s", response.getError().getMessage()));
                return new ResultData<>(response.getError().getCode(), response.getError().getMessage());
            }
            log.info("HuMo card verify response: {}", response);
            return ResultData.ok(response);
        } catch (Exception e) {
            throw new GatewayException(e.getMessage());
        }
    }


    @Override
    public ResultData<HuMoCardVerifyBaseResponse<HuMoCardConfirmResult>> confirm(@NonNull CardConfirmRequestDTO requestDTO) {
        log.info("HuMo card confirm request: {}", requestDTO);
        HuMoCardBaseRequest<HuMoCardConfirmParams> request = new HuMoCardBaseRequest<>(
                "humo_integration_" + System.currentTimeMillis(),
                new HuMoCardConfirmParams(
                        requestDTO.getPan(),
                        requestDTO.getCode()
                )
        );

        try {
            HuMoCardVerifyBaseResponse<HuMoCardConfirmResult> response = client.confirm(request);
            log.info("HuMo card confirm response: {}", response);
            return ResultData.ok(response);
        } catch (Exception e) {
            log.error("confirm card method throws exception: {}", e.getMessage());
            throw new GatewayException(e.getMessage());
        }
    }
}
