package uz.davrbank.usermanualservice.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 26.11.2025
 * Time: 10.35
 * Project: usermanual-service
 */

public record UserManualSectionDetailDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("children") List<UserManualSectionDetailDTO> children
) {
}