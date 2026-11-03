package uz.davrbank.usermanualservice.aggregate.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualSectionDTO;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualSectionDetailDTO;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualSectionListDTO;
import uz.davrbank.usermanualservice.aggregate.enums.PlatformType;
import uz.davrbank.usermanualservice.aggregate.model.ResultData;
import uz.davrbank.usermanualservice.aggregate.service.UserManualService;
import uz.davrbank.usermanualservice.utils.constants.ApiConstants;

import java.util.EnumSet;
import java.util.List;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

@RestController
@RequestMapping(ApiConstants.userManualRootApi)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserManualSectionController {

    UserManualService service;

    @GetMapping
    public ResponseEntity<ResultData<List<UserManualSectionListDTO>>> getAll(@RequestParam("platformType") String platformType) {
        return ResponseEntity.ok(service.getAllSectionsTree(platformType));
    }

    @PostMapping
    public ResponseEntity<ResultData<Long>> create(@RequestBody UserManualSectionDTO request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping(ApiConstants.id)
    public ResponseEntity<ResultData<Long>> update(@PathVariable Long id, @RequestBody UserManualSectionDTO req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping(ApiConstants.id)
    public ResponseEntity<ResultData<Long>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @GetMapping(ApiConstants.id)
    public ResponseEntity<ResultData<UserManualSectionDetailDTO>> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDetail(id));
    }

    @GetMapping("/platform-types")
    ResponseEntity<ResultData<EnumSet<PlatformType>>> getStatuses() {
        return ResponseEntity.ok(ResultData.ok(EnumSet.allOf(PlatformType.class)));
    }
}
