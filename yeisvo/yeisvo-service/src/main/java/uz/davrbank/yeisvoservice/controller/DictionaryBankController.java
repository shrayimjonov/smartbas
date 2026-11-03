package uz.davrbank.yeisvoservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.yeisvoservice.aggregate.dto.request.DictionaryBankReq;
import uz.davrbank.yeisvoservice.aggregate.dto.response.spravochniki.*;
import uz.davrbank.yeisvoservice.service.service.sevices.DictionaryBankService;

@RestController
@RequestMapping("/api/v1/dictionary")
@RequiredArgsConstructor
public class DictionaryBankController {

    private final DictionaryBankService dictionaryBankService;

    // country list
    @PostMapping("/country")
    public ResponseEntity<DicCountryResponse> dicCountry(@RequestBody DictionaryBankReq request) {
        return ResponseEntity.ok(dictionaryBankService.dicCountry(request));
    }

    // contract subject list
    @PostMapping("/contract/subject")
    public ResponseEntity<DicContractSubjectsResponse> getDicContractSubjects(@RequestBody DictionaryBankReq request) {
        return ResponseEntity.ok(dictionaryBankService.getDicContractSubjects(request));
    }

    // country by id
    @GetMapping("/country/{id}")
    public ResponseEntity<DicCountryResponse> getDicCountryById(@PathVariable String id) {
        return ResponseEntity.ok(dictionaryBankService.getDicCountryById(id));
    }

    @GetMapping("/contract/subject/{id}")
    public ResponseEntity<DicContractSubjectsResponse> getDicContractSubjectsById(@PathVariable Long id) {
        return ResponseEntity.ok(dictionaryBankService.getDicContractSubjectsById(id));
    }

    @PostMapping("/region")
    public ResponseEntity<RegionsResponse> dicRegion(@RequestBody DictionaryBankReq request) {
        return ResponseEntity.ok(dictionaryBankService.dicRegion(request));
    }


    @PostMapping("/industrial/zone")
    public ResponseEntity<IndustrialZoneResponse> dicIndustrialZone(@RequestBody DictionaryBankReq request) {
        return ResponseEntity.ok(dictionaryBankService.dicIndustrialZone(request));
    }

    // bank info list
    @PostMapping("/bank/info")
    public ResponseEntity<DictionaryBankResponse> dicBankResponse(@RequestBody DictionaryBankReq request) {
        return ResponseEntity.ok(dictionaryBankService.dicBankResponse(request));
    }

    // region by id
    @GetMapping("/region/{id}")
    public ResponseEntity<RegionsResponse> getDicRegionById(@PathVariable String id) {
        return ResponseEntity.ok(dictionaryBankService.getDicRegionById(id));
    }

    // industrial zone by id
    @GetMapping("/industrial/zone/{id}")
    public ResponseEntity<IndustrialZoneResponse> getDicIndustrialZoneById(@PathVariable Long id) {
        return ResponseEntity.ok(dictionaryBankService.getDicIndustrialZoneById(id));
    }

    // bank info by id
    @GetMapping("/bank/info/{id}")
    public ResponseEntity<DictionaryBankResponse> getDicBankId(@PathVariable Long id) {
        return ResponseEntity.ok(dictionaryBankService.getDicBankId(id));
    }
}

