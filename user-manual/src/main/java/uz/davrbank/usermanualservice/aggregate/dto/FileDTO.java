package uz.davrbank.usermanualservice.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 25.11.2025
 * Time: 15:42
 * Project: usermanual-service
 */

public record FileDTO(
        @JsonProperty("file_name") String fileName,
        @JsonProperty("path") String path
) {
}