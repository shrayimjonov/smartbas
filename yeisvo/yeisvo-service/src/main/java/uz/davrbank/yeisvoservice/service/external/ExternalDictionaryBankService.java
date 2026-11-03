package uz.davrbank.yeisvoservice.service.external;

import uz.davrbank.yeisvoservice.aggregate.dto.request.DictionaryBankReq;
import uz.davrbank.yeisvoservice.aggregate.dto.response.spravochniki.*;
import uz.davrbank.yeisvoservice.aggregate.prop.ExternalProp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExternalDictionaryBankService extends YeisvoRequestService {
    private final ExtBusYeisvoService extBusYeisvoService;

    protected ExternalDictionaryBankService(ExternalProp externalProp, ExternalConnector externalConnector, GenerateToken generateToken, ExtBusYeisvoService extBusYeisvoService) {
        super(externalProp.getConfig("yeisvo"), externalConnector, generateToken);
        this.extBusYeisvoService = extBusYeisvoService;
    }

    public DicCountryResponse getDicCountry(DictionaryBankReq request) {
        DicCountryResponse yeisvoRes = externalCallPost(
                "additional/eisvo-country",
                request,
                new ParameterizedTypeReference<>() {
                });
        if (yeisvoRes != null && (Integer.valueOf(0).equals(yeisvoRes.getStatus()) && "OK".equals(yeisvoRes.getErrorMsg()))) {
            return yeisvoRes;
        }
        return extBusYeisvoService.checkResponseStatus(yeisvoRes);
    }

    public DicContractSubjectsResponse getDicContractSubjects(DictionaryBankReq request) {
        DicContractSubjectsResponse yeisvoRes = externalCallPost(
                "additional/eisvo-contract-subject",
                request,
                new ParameterizedTypeReference<>() {
                });
        if (yeisvoRes != null && (Integer.valueOf(0).equals(yeisvoRes.getStatus()) && "OK".equals(yeisvoRes.getErrorMsg()))) {
            return yeisvoRes;
        }
        return extBusYeisvoService.checkResponseStatus(yeisvoRes);
    }

    public DictionaryBankResponse getDicBank(DictionaryBankReq request) {
        DictionaryBankResponse yeisvoRes = externalCallPost(
                "additional/eisvo-contract-status", // ozgartirish kk
                request,
                new ParameterizedTypeReference<>() {
                });
        if (yeisvoRes != null && (Integer.valueOf(0).equals(yeisvoRes.getStatus()) && "OK".equals(yeisvoRes.getErrorMsg()))) {
            return yeisvoRes;
        }
        return extBusYeisvoService.checkResponseStatus(yeisvoRes);
    }

    public RegionsResponse getRegion(DictionaryBankReq request) {
        RegionsResponse yeisvoRes = externalCallPost(
                "additional/eisvo-regions-info",
                request,
                new ParameterizedTypeReference<>() {
                });
        if (yeisvoRes != null && (Integer.valueOf(0).equals(yeisvoRes.getStatus()) && "OK".equals(yeisvoRes.getErrorMsg()))) {
            return yeisvoRes;
        }
        return extBusYeisvoService.checkResponseStatus(yeisvoRes);
    }

    public IndustrialZoneResponse getIndustrialZone(DictionaryBankReq request) {
        IndustrialZoneResponse yeisvoRes = externalCallPost(
                "additional/eisvo-industrial-zone",
                request,
                new ParameterizedTypeReference<>() {
                });
        if (yeisvoRes != null && (Integer.valueOf(0).equals(yeisvoRes.getStatus()) && "OK".equals(yeisvoRes.getErrorMsg()))) {
            return yeisvoRes;
        }
        return extBusYeisvoService.checkResponseStatus(yeisvoRes);
    }

    public DictionaryBankResponse getBankInfo(DictionaryBankReq request) {
        DictionaryBankResponse yeisvoRes = externalCallPost(
                "additional/eisvo-bank-info",
                request,
                new ParameterizedTypeReference<>() {
                });
        if (yeisvoRes != null && (Integer.valueOf(0).equals(yeisvoRes.getStatus()) && "OK".equals(yeisvoRes.getErrorMsg()))) {
            return yeisvoRes;
        }
        return extBusYeisvoService.checkResponseStatus(yeisvoRes);
    }


}
