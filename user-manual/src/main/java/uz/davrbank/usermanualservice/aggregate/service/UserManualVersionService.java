package uz.davrbank.usermanualservice.aggregate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.usermanualservice.aggregate.domain.UserManualVersion;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualVersionDTO;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualVersionDetailDTO;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualVersionListDTO;
import uz.davrbank.usermanualservice.aggregate.dto.base.CommonDTO;
import uz.davrbank.usermanualservice.aggregate.enums.UserManualVersionStatus;
import uz.davrbank.usermanualservice.aggregate.model.ResultData;
import uz.davrbank.usermanualservice.aggregate.repository.UserManualVersionRepository;
import uz.davrbank.usermanualservice.exception.condition.BadRequestException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserManualVersionService {

    UserManualVersionRepository userManualVersionRepository;

    @Transactional
    public ResultData<Long> create(UserManualVersionDTO request) {
        if (request.versionNumber() == null || request.versionNumber().isBlank()) {
            throw new BadRequestException("version_number is required");
        }

        if (userManualVersionRepository.existsByVersionNumberIgnoreCaseAndSectionId(request.versionNumber(), request.sectionId())) {
            throw new BadRequestException("User manual section with this version already exists");
        }
        UserManualVersion userManualVersion = new UserManualVersion();
        userManualVersion.setVersionNumber(request.versionNumber());
        userManualVersion.setSectionId(request.sectionId());
        return ResultData.ok(userManualVersionRepository.save(userManualVersion).getId());
    }

    @Transactional
    public ResultData<Long> update(Long id, UserManualVersionDTO request) {
        if (request.versionNumber() == null || request.versionNumber().isBlank()) {
            throw new BadRequestException("version_number is required");
        }
        return userManualVersionRepository.findById(id).map(userManualVersion -> {
            if (userManualVersionRepository.existsByVersionNumberIgnoreCaseAndSectionIdAndIdNot(request.versionNumber(), request.sectionId(), id)) {
                throw new BadRequestException("User manual section with this version already exists");
            }
            userManualVersion.setVersionNumber(request.versionNumber());
            userManualVersion.setSectionId(request.sectionId());
            userManualVersionRepository.save(userManualVersion);
            return ResultData.ok(userManualVersion.getId());
        }).orElseThrow(() -> new BadRequestException("user_manual_version not found"));
    }

    @Transactional
    public ResultData<Long> delete(Long id) {
        return userManualVersionRepository.findById(id).map(userManualVersion -> {
            if (UserManualVersionStatus.DRAFT != userManualVersion.getStatus()) {
                throw new BadRequestException("Only draft can be deleted");
            }
            userManualVersionRepository.deleteById(id);
            return ResultData.ok(userManualVersion.getId());
        }).orElseThrow(() -> new BadRequestException("user_manual_version not found"));
    }

    @Transactional
    public ResultData<Long> updateStatus(Long id, UserManualVersionStatus status) {
        if (status == null) {
            throw new BadRequestException("status is required");
        }
        return userManualVersionRepository.findById(id).map(userManualVersion -> {
            UserManualVersionStatus currentStatus = userManualVersion.getStatus();
            if (currentStatus == status) {
                return ResultData.ok(userManualVersion.getId());
            }

            boolean allowed = switch (currentStatus) {
                case DRAFT -> status == UserManualVersionStatus.PUBLISHED; // only draft->published
                case PUBLISHED -> status == UserManualVersionStatus.ARCHIVED; // published->archived
                case ARCHIVED -> status == UserManualVersionStatus.PUBLISHED; // archived->published
            };

            if (!allowed) {
                throw new BadRequestException("Invalid status transition: " + currentStatus + " -> " + status);
            }
            userManualVersion.setStatus(status);
            return ResultData.ok(userManualVersion.getId());
        }).orElseThrow(() -> new BadRequestException("user_manual_version not found"));
    }

    @Transactional(readOnly = true)
    public ResultData<List<UserManualVersionListDTO>> getList(String status, Long sectionId) {
        UserManualVersionStatus st = UserManualVersionStatus.valueOf(status);
        List<UserManualVersion> list = userManualVersionRepository.findAllByStatusAndSectionIdOrderByIdAsc(st, sectionId);
        List<UserManualVersionListDTO> listDTOS = list.stream().map(v ->
                new UserManualVersionListDTO(
                        v.getId(),
                        v.getVersionNumber(),
                        new CommonDTO(v.getSectionId(), v.getSection().getName()),
                        v.getStatus().name()
                )
        ).collect(Collectors.toList());
        return ResultData.ok(listDTOS);
    }

    @Transactional(readOnly = true)
    public ResultData<UserManualVersionDetailDTO> getDetail(Long id) {
        return userManualVersionRepository.findById(id)
                .map(v -> ResultData.ok(new UserManualVersionDetailDTO(
                        v.getId(),
                        v.getVersionNumber(),
                        new CommonDTO(v.getSectionId(), v.getSection() != null ? v.getSection().getName() : null),
                        v.getStatus().name()
                )))
                .orElseThrow(() -> new BadRequestException("user_manual_version not found"));
    }
}
