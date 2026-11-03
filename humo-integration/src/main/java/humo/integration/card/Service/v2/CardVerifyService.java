package humo.integration.card.Service.v2;

import humo.integration.card.Model.Dto.core.request.CardConfirmRequestDTO;
import humo.integration.card.Model.Dto.core.request.CardVerifyRequestDTO;
import humo.integration.card.Model.Dto.core.response.ResultData;
import humo.integration.card.Model.Response.v2.HuMoCardVerifyBaseResponse;
import humo.integration.card.Model.Response.v2.result.HuMoCardConfirmResult;
import humo.integration.card.Model.Response.v2.result.HuMoCardVerifyResult;
import lombok.NonNull;

public interface CardVerifyService {
    ResultData<HuMoCardVerifyBaseResponse<HuMoCardVerifyResult>> verify(@NonNull CardVerifyRequestDTO requestDTO);

    ResultData<HuMoCardVerifyBaseResponse<HuMoCardConfirmResult>> confirm(@NonNull CardConfirmRequestDTO requestDTO);


}
