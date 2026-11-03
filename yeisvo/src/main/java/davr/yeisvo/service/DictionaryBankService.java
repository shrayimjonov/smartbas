package davr.yeisvo.service;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import davr.yeisvo.dto.request.DictionaryBankReq;
import davr.yeisvo.dto.response.spravochniki.*;

@JsonRpcService("api/v1/dictionary")
public interface DictionaryBankService {

    @JsonRpcMethod("country")
    DicCountryResponse dicCountry(@JsonRpcParam("request") DictionaryBankReq request);

    @JsonRpcMethod("contract-subject")
    DicContractSubjectsResponse getDicContractSubjects(@JsonRpcParam("request") DictionaryBankReq request);

    @JsonRpcMethod("get.country.by.id")
    DicCountryResponse getDicCountryById(@JsonRpcParam("request") String id);

    @JsonRpcMethod("get.contract.subject.by.id")
    DicContractSubjectsResponse getDicContractSubjectsById(@JsonRpcParam("request") Long id);

    @JsonRpcMethod("region")
    RegionsResponse dicRegion(@JsonRpcParam("request") DictionaryBankReq request);

    @JsonRpcMethod("industrial.zone")
    IndustrialZoneResponse dicIndustrialZone(@JsonRpcParam("request") DictionaryBankReq request);

    @JsonRpcMethod("bank.info")
    DictionaryBankResponse dicBankResponse(@JsonRpcParam("request") DictionaryBankReq request);


    @JsonRpcMethod("get.region.by.id")
    RegionsResponse getDicRegionById(@JsonRpcParam("request") String id);

    @JsonRpcMethod("get.industrial.zone.by.id")
    IndustrialZoneResponse getDicIndustrialZoneById(@JsonRpcParam("request") Long id);

    @JsonRpcMethod("get.bank.info.by.id")
    DictionaryBankResponse getDicBankId(@JsonRpcParam("request") Long id);
}
