package uz.davrbank.usermanualservice.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

public record UserManualSectionDTO(
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("parent_id") Long parentId,
        @JsonProperty("platform_type") String platformType
) {
}