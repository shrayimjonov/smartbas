package uz.davrbank.yeisvoservice.service.external;


import uz.davrbank.yeisvoservice.aggregate.dto.request.ContractListRequestBody;
import uz.davrbank.yeisvoservice.aggregate.dto.request.ContractRequestBody;
import uz.davrbank.yeisvoservice.aggregate.dto.response.*;
import uz.davrbank.yeisvoservice.aggregate.prop.ExternalProp;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@Service
@Slf4j
public class ExtBusYeisvoService extends YeisvoRequestService {

    private final Logger logger = LogManager.getFormatterLogger(ExtBusYeisvoService.class);

    protected ExtBusYeisvoService(ExternalProp externalProp, ExternalConnector externalConnector, GenerateToken tokenGenerator) {
        super(externalProp.getConfig("yeisvo"), externalConnector, tokenGenerator);
    }

    public ContractGetStatusRes getContractStatus(ContractRequestBody request) {
        ContractGetStatusRes yesisvoRes = externalCallPost(
                "additional/eisvo-contract-status",
                request,
                new ParameterizedTypeReference<>() {
                });
        logger.info("yesisvo response for contract status : {}", yesisvoRes.getCntrStatus());
        if (yesisvoRes != null && (Integer.valueOf(0).equals(yesisvoRes.getStatus()) && "OK".equals(yesisvoRes.getErrorMsg()))) {
            return yesisvoRes;
        }
        return checkResponseStatus(yesisvoRes);
    }

    public ContractGetListRes getContractList(ContractListRequestBody request) {
        ContractGetListRes yesisvoRes = externalCallPost(
                "additional/eisvo-contract-list",
                request,
                new ParameterizedTypeReference<>() {
                });

        if (yesisvoRes != null && (Integer.valueOf(0).equals(yesisvoRes.getStatus()) && "OK".equals(yesisvoRes.getErrorMsg()))) {
            return yesisvoRes;
        }
        return checkResponseStatus(yesisvoRes);
    }

    public ContractGetOwnerDocRes getContractOwnerDocs(ContractRequestBody request) {
        ContractGetOwnerDocRes yesisvoRes = externalCallPost(
                "additional/eisvo-get-owner",
                request,
                new ParameterizedTypeReference<>() {
                });

        if (yesisvoRes != null && (Integer.valueOf(0).equals(yesisvoRes.getStatus()) && "OK".equals(yesisvoRes.getErrorMsg()))) {
            return yesisvoRes;
        }
        return checkResponseStatus(yesisvoRes);
    }

    public ContractGetHistoryRes getContractHistoryDocs(ContractRequestBody request) {
        ContractGetHistoryRes yesisvoRes = externalCallPost(
                "additional/eisvo-contract-history",
                request,
                new ParameterizedTypeReference<>() {
                });

        if (yesisvoRes != null && (Integer.valueOf(0).equals(yesisvoRes.getStatus()) && "OK".equals(yesisvoRes.getErrorMsg()))) {
            return yesisvoRes;
        }
        return checkResponseStatus(yesisvoRes);
    }


    public ContractGetBankDocsRes getBankDocs(ContractRequestBody request) {
        ContractGetBankDocsRes yesisvoRes = externalCallPost(
                "additional/eisvo-bank-docs",
                request,
                new ParameterizedTypeReference<>() {
                });

        if (yesisvoRes != null && (Integer.valueOf(0).equals(yesisvoRes.getStatus()) && "OK".equals(yesisvoRes.getErrorMsg()))) {
            return yesisvoRes;
        }
        return checkResponseStatus(yesisvoRes);
    }

    public ContractGetRes getContract(ContractRequestBody request) {
        ContractGetRes yesisvoRes = externalCallPost(
                "additional/eisvo-get-contract",
                request,
                new ParameterizedTypeReference<>() {
                });

        if (yesisvoRes != null && (Integer.valueOf(0).equals(yesisvoRes.getStatus()) && "OK".equals(yesisvoRes.getErrorMsg()))) {
            return yesisvoRes;
        }
        return checkResponseStatus(yesisvoRes);
    }

    public  <RES> RES checkResponseStatus(RES response) {
        if (response == null) {
            String errorMsg = "Response is null";
            logger.error("Yeisvo service error: %s, operation id %s", errorMsg, UUID.randomUUID());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMsg);
        }
        try {
            if (Integer.valueOf(-994).equals(response.getClass().getMethod("getStatus").invoke(response))) {
                logger.error("Yeisvo error: %s, operation id %s", response.getClass().getMethod("getErrorMsg").invoke(response), UUID.randomUUID());
                return response;
            } if (Integer.valueOf(-129).equals(response.getClass().getMethod("getStatus").invoke(response))) {
                logger.error("Yeisvo error : %s, operation id %s", response.getClass().getMethod("getErrorMsg").invoke(response), UUID.randomUUID());
                return response;
            }else if (Integer.valueOf(-3).equals(response.getClass().getMethod("getStatus").invoke(response))) {
                logger.error("Yeisvo error: %s, operation id %s", response.getClass().getMethod("getErrorMsg").invoke(response), UUID.randomUUID());
                return response;
            } else if (Integer.valueOf(-97).equals(response.getClass().getMethod("getStatus").invoke(response))) {
                logger.error("Yeisvo error: %s, operation id %s", response.getClass().getMethod("getErrorMsg").invoke(response), UUID.randomUUID());
                return response;
            } else if (Integer.valueOf(-1).equals(response.getClass().getMethod("getStatus").invoke(response))) {
                logger.error("Data is not found !");
                return response;
            } else {
                logger.error("Yeisvo service error: %s, operation id %s", response.getClass().getMethod("getErrorMsg").invoke(response), UUID.randomUUID());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, response.getClass().getMethod("getErrorMsg").invoke(response).toString());
            }
        } catch (NoSuchMethodException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

