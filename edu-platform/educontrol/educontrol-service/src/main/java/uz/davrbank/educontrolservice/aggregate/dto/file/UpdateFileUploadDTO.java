package uz.davrbank.educontrolservice.aggregate.dto.file;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateFileUploadDTO(
        @JsonProperty("is_active")
        Boolean isActive,
        @JsonProperty("resource_id")
        Long resourceId,
        @JsonProperty("resource_type")
        ResourceType resourceType,
        String description) {
}