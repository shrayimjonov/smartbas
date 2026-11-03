package uz.davrbank.usermanualservice.aggregate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.usermanualservice.aggregate.domain.UserManualSection;
import uz.davrbank.usermanualservice.aggregate.domain.base.BaseEntity;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualSectionListDTO;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualSectionDetailDTO;
import uz.davrbank.usermanualservice.aggregate.dto.UserManualSectionDTO;
import uz.davrbank.usermanualservice.aggregate.enums.PlatformType;
import uz.davrbank.usermanualservice.aggregate.model.ResultData;
import uz.davrbank.usermanualservice.aggregate.repository.UserManualSectionRepository;
import uz.davrbank.usermanualservice.aggregate.repository.UserManualVersionRepository;
import uz.davrbank.usermanualservice.exception.condition.BadRequestException;

import java.util.*;
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
public class UserManualService {

    UserManualSectionRepository userManualSectionRepository;
    UserManualVersionRepository userManualVersionRepository;

    @Transactional
    public ResultData<Long> create(UserManualSectionDTO request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new BadRequestException("title is required");
        }
        if (request.platformType() == null || request.platformType().isBlank()) {
            throw new BadRequestException("platformType is required");
        }
        if (userManualSectionRepository.existsByNameIgnoreCase(request.title())) {
            throw new BadRequestException("title must be unique");
        }
        PlatformType platformType = PlatformType.valueOf(request.platformType());
        UserManualSection manual = new UserManualSection();
        manual.setName(request.title());
        manual.setDescription(request.description());
        manual.setParentId(request.parentId());
        manual.setPlatformType(platformType);
        return ResultData.ok(userManualSectionRepository.save(manual).getId());
    }

    @Transactional
    public ResultData<Long> update(Long id, UserManualSectionDTO request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new BadRequestException("title is required");
        }
        return userManualSectionRepository.findById(id).map(userManualSection -> {
            if (userManualSectionRepository.existsByNameIgnoreCaseAndIdNot(request.title(), id)) {
                throw new BadRequestException("title must be unique");
            }
            userManualSection.setName(request.title());
            userManualSection.setDescription(request.description());
            userManualSection.setParentId(request.parentId());
            if (request.platformType() != null && !request.platformType().isBlank()) {
                userManualSection.setPlatformType(PlatformType.valueOf(request.platformType()));
            }
            userManualSectionRepository.save(userManualSection);
            return ResultData.ok(userManualSection.getId());
        }).orElseThrow(() -> new RuntimeException("User manual section not found"));
    }

    @Transactional
    public ResultData<Long> delete(Long id) {
        if (userManualSectionRepository.existsByParentId(id)) {
            throw new BadRequestException("This section has child section. Firstly, delete them!!!");
        }
        if (userManualVersionRepository.existsBySectionId(id)) {
            throw new BadRequestException("This section has document, delete documents first.");
        }
        userManualSectionRepository.deleteById(id);
        return ResultData.ok(id);
    }

    public ResultData<List<UserManualSectionListDTO>> getAllSectionsTree(String type) {
        if (type == null || type.isBlank()) {
            throw new BadRequestException("platformType is required");
        }
        PlatformType platformType =PlatformType.valueOf(type);
        List<UserManualSection> all = userManualSectionRepository.findAllByPlatformTypeOrderByIdAsc(platformType);
        Map<Long, List<UserManualSection>> childrenByParent = new HashMap<>();
        for (UserManualSection s : all) {
            Long parentId = s.getParentId();
            childrenByParent.computeIfAbsent(parentId, k -> new ArrayList<>()).add(s);
        }
        List<UserManualSection> roots = childrenByParent.getOrDefault(null, List.of())
                .stream()
                .sorted(Comparator.comparingLong(BaseEntity::getId))
                .toList();
        List<UserManualSectionListDTO> tree = roots.stream()
                .map(s -> toDtoRecursive(s, childrenByParent))
                .collect(Collectors.toList());
        return ResultData.ok(tree);
    }

    private UserManualSectionListDTO toDtoRecursive(UserManualSection section, Map<Long, List<UserManualSection>> childrenByParent) {
        List<UserManualSection> children = childrenByParent.getOrDefault(section.getId(), List.of())
                .stream()
                .sorted(Comparator.comparingLong(BaseEntity::getId))
                .toList();
        List<UserManualSectionListDTO> childElement = children.stream()
                .map(child -> toDtoRecursive(child, childrenByParent))
                .collect(Collectors.toList());
        return new UserManualSectionListDTO(
                section.getId(),
                section.getName(),
                section.getDescription(),
                childElement
        );
    }

    @Transactional(readOnly = true)
    public ResultData<UserManualSectionDetailDTO> getDetail(Long id) {
        UserManualSection target = userManualSectionRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("user_manual_section not found"));
        return ResultData.ok(toDetailDtoRecursive(target));
    }

    private UserManualSectionDetailDTO toDetailDtoRecursive(UserManualSection section) {
        List<UserManualSection> children = userManualSectionRepository
                .findAllByParentIdOrderByIdAsc(section.getId());
        List<UserManualSectionDetailDTO> childElement = children.stream()
                .map(this::toDetailDtoRecursive)
                .collect(Collectors.toList());
        return new UserManualSectionDetailDTO(
                section.getId(),
                section.getName(),
                section.getDescription(),
                childElement
        );
    }
}
