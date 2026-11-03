package uz.davrbank.userinfobase.dto.response.files;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Lob;
import uz.davrbank.userinfobase.entity.files.FileUpload;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FileUploadResponse(
        Long id,
        String fileName,
        @Lob byte[] file,
        String file_size,
        String file_type,
        LocalDateTime uploadedAt
) {

    public static FileUploadResponse fromEntity(FileUpload fileUpload) {
        return new FileUploadResponse(
                fileUpload.getId(),
                fileUpload.getFileName(),
                fileUpload.getFile(),
                formatFileSize(fileUpload.getFileSize() != null ? fileUpload.getFileSize() : 0),
                fileUpload.getFileType(),
                fileUpload.getUploadedAt()
        );
    }

    public static String formatFileSize(Long size) {
        if (size == null || size <= 0) return "0 B";
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = (int) (Math.log10(size) / Math.log10(1024));
        double formattedSize = size / Math.pow(1024, unitIndex);
        return String.format("%.2f %s", formattedSize, units[unitIndex]);
    }
}