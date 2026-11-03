package uz.davrbank.userinfobase.dto;


import uz.davrbank.userinfobase.dto.response.files.FileUploadResponse;

import java.util.List;

public record UploadProgressResponse(
        List<FileUploadResponse> uploadedFiles,
        int remaining,
        String message
) {
}