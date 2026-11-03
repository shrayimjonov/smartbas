package uz.davrbank.yeisvoservice.service.service.interfaces;

import uz.davrbank.yeisvoservice.aggregate.dto.request.DictionaryBankReq;
import uz.davrbank.yeisvoservice.aggregate.dto.response.spravochniki.*;


public interface DictionaryBankInterface {

    DicCountryResponse dicCountry(DictionaryBankReq request);

    DicContractSubjectsResponse getDicContractSubjects( DictionaryBankReq request);

    DicCountryResponse getDicCountryById( String id);

    DicContractSubjectsResponse getDicContractSubjectsById( Long id);

    RegionsResponse dicRegion( DictionaryBankReq request);

    IndustrialZoneResponse dicIndustrialZone(DictionaryBankReq request);

    DictionaryBankResponse dicBankResponse( DictionaryBankReq request);

    RegionsResponse getDicRegionById( String id);

    IndustrialZoneResponse getDicIndustrialZoneById(Long id);

    DictionaryBankResponse getDicBankId( Long id);

}
