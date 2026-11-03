package humo.integration.card.Feign.HumoFeign;

import humo.integration.card.Feign.FeignConfig.FeignCardConfig;
import humo.integration.card.Model.Request.MaskedPanReq;
import humo.integration.card.Model.Request.RealPanReq;
import humo.integration.card.Model.Request.v2.HuMoCardBaseRequest;
import humo.integration.card.Model.Request.v2.params.HuMoCardConfirmParams;
import humo.integration.card.Model.Request.v2.params.HuMoCardVerifyParams;
import humo.integration.card.Model.Response.CardMaskedPanResp;
import humo.integration.card.Model.Response.CardRealPanResp;
import humo.integration.card.Model.Response.MaskedPanResp;
import humo.integration.card.Model.Response.RealPanResp;
import humo.integration.card.Model.Response.v2.HuMoCardVerifyBaseResponse;
import humo.integration.card.Model.Response.v2.result.HuMoCardConfirmResult;
import humo.integration.card.Model.Response.v2.result.HuMoCardVerifyResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "humoCard", url = "${humo.middleware}", configuration = {FeignCardConfig.class})
public interface HumoCardFeign {


    @GetMapping("/api/getMaskedPan?pan={pan}")
    CardMaskedPanResp getHumoToken(@PathVariable String pan);

    @GetMapping("/api/getRealPan?masked_pan={masked_pan}")
    CardRealPanResp getHumoRealPan(@PathVariable String masked_pan);

    @PostMapping("/v2/pm/masked-pan")
    MaskedPanResp getHumoMaskedPan(@RequestBody MaskedPanReq maskedPanReq);

    @PostMapping("/v2/pm/real-pan")
    RealPanResp getHumoPan(@RequestBody RealPanReq realPanReq);

    @PostMapping("/v1/send/sms")
    HuMoCardVerifyBaseResponse<HuMoCardVerifyResult> verify(HuMoCardBaseRequest<HuMoCardVerifyParams> request);

    @PostMapping("/v1/send/code")
    HuMoCardVerifyBaseResponse<HuMoCardConfirmResult> confirm(HuMoCardBaseRequest<HuMoCardConfirmParams> request);

}
