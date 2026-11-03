package uz.davrbank.usermanualservice.aggregate.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualVersionDTO;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualVersionDetailDTO;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualVersionListDTO;
import uz.davrbank.usermanualservice.aggregate.enums.UserManualVersionStatus;
import uz.davrbank.usermanualservice.aggregate.model.ResultData;
import uz.davrbank.usermanualservice.aggregate.service.UserManualVersionService;
import uz.davrbank.usermanualservice.utils.constants.ApiConstants;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

@RestController
@RequestMapping(ApiConstants.manualVersionRootApi)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserManualVersionController {

    UserManualVersionService service;

    @GetMapping
    public ResponseEntity<ResultData<List<UserManualVersionListDTO>>> getByStatusAndSection(@RequestParam(value = "status", required = false) String status,
                                                                                            @RequestParam("sectionId") Long sectionId) {
        return ResponseEntity.ok(service.getList(Optional.ofNullable(status).orElse(UserManualVersionStatus.PUBLISHED.name()), sectionId));
    }

    @PostMapping
    public ResponseEntity<ResultData<Long>> create(@RequestBody UserManualVersionDTO req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping(ApiConstants.id)
    public ResponseEntity<ResultData<Long>> update(@PathVariable Long id, @RequestBody UserManualVersionDTO req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping(ApiConstants.id)
    public ResponseEntity<ResultData<Long>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.delete(id));
    }

    @PatchMapping(ApiConstants.id)
    public ResponseEntity<ResultData<Long>> updateStatus(@PathVariable Long id, @RequestParam("status") String status) {
        return ResponseEntity.ok(service.updateStatus(id, UserManualVersionStatus.valueOf(status)));
    }

    @GetMapping(ApiConstants.id)
    public ResponseEntity<ResultData<UserManualVersionDetailDTO>> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDetail(id));
    }

    @GetMapping("/statuses")
    ResponseEntity<ResultData<EnumSet<UserManualVersionStatus>>> getStatuses() {
        return ResponseEntity.ok(ResultData.ok(EnumSet.allOf(UserManualVersionStatus.class)));
    }
}
