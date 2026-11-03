package uz.davrbank.usermanualservice.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */
public record UserManualVersionDTO(
        @JsonProperty("version_number") String versionNumber,
        @JsonProperty("section_id") Long sectionId
) {}