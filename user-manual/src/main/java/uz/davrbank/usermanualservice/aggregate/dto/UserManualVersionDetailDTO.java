package uz.davrbank.usermanualservice.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.davrbank.usermanualservice.aggregate.dto.base.CommonDTO;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 26.11.2025
 * Time: 10.35
 * Project: usermanual-service
 */

public record UserManualVersionDetailDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("version_number") String versionNumber,
        @JsonProperty("section") CommonDTO section,
        @JsonProperty("status") String status
) {
}