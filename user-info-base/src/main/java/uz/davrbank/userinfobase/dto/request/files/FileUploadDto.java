package uz.davrbank.userinfobase.dto.request.files;

import jakarta.persistence.Lob;
import lombok.*;

import java.time.LocalDateTime;


import jakarta.persistence.Lob;
import java.time.LocalDateTime;

public record FileUploadDto(
        String fileName,
        @Lob byte[] file,
        LocalDateTime uploadedAt
) {}