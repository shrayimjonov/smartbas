package uz.davrbank.usermanualservice.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 25.11.2025
 * Time: 15:42
 * Project: usermanual-service
 */

public record FileUploadDTO(
        @JsonProperty("file_name") String fileName,
        @JsonProperty("version") Long versionId,
        @JsonProperty("language") String language,
        @JsonProperty("content") String contentBase64
) {
}