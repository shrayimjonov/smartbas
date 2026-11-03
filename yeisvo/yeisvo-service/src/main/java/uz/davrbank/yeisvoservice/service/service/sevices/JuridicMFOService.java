package uz.davrbank.yeisvoservice.service.service.sevices;
import uz.davrbank.yeisvoservice.Feign.YeisvoHubClient;
import uz.davrbank.yeisvoservice.aggregate.dto.request.JuridicClientRequest;
import uz.davrbank.yeisvoservice.aggregate.dto.response.ResponseInnDTO;
import uz.davrbank.yeisvoservice.aggregate.dto.response.report.ResponseDTO;
import uz.davrbank.yeisvoservice.entity.BankInfo;
import uz.davrbank.yeisvoservice.exception.controller.GlobalExceptionMessage;
import uz.davrbank.yeisvoservice.service.service.interfaces.JuridicMFOInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class JuridicMFOService implements JuridicMFOInterface {
    private final YeisvoHubClient yeisvoHubClient;

    private static final Set<BankInfo> ALLOWED_BANKS = Set.of(
            new BankInfo("00981", "203709707"),
            new BankInfo("01122", "207212342"),
            new BankInfo("01046", "206973924"),
            new BankInfo("01069", "207076924"),
            new BankInfo("01072", "207094902"),
            new BankInfo("01086", "207128010"),
            new BankInfo("01121", "207212359"));


    public JuridicMFOService(YeisvoHubClient yeisvoHubClient) {
        this.yeisvoHubClient = yeisvoHubClient;
    }

    @Override
    public ResponseDTO<List<ResponseInnDTO>> getJuridicMFO(JuridicClientRequest request) {
        log.info("getJuridicMFO request {}", request);
        ResponseDTO<List<ResponseInnDTO>> all = new ResponseDTO<>();
        try {
            all = yeisvoHubClient.getAll(request.getBankId(), String.valueOf(request.getActive()));
        } catch (Exception ex) {
            throw new GlobalExceptionMessage(400, ex.getMessage(), request.getActive());
        }

        log.info("Came getJuridicMFO list from yeisvo hub {}", all.getData().size());
        if (!all.getData().isEmpty()) {
            log.info("getJuridicMFO response Size: {}", all.getData().size());

            return all;
        }

        log.info("getJuridicMFO empty response");

        return all;
    }

}
