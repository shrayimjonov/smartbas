package uz.davrbank.userinfobase.dto;

import uz.davrbank.userinfobase.entity.DavrhubFile;
import uz.davrbank.userinfobase.service.HashIdService;

public record DavrhubDtoResponse(
        Long id,
        String fileName,
        String dtime,
        String fileSize,
        String fileUrl
) {
    private static final String DAVRHUB_DOWNLOAD_URL_PREFIX =
            "https://internal.example.com/back/api/davrhub/download/";

    public static DavrhubDtoResponse fromEntity(DavrhubFile entity, HashIdService hashIdService) {
        String hashId = hashIdService.encode(entity.getId());
        return new DavrhubDtoResponse(
                entity.getId(),
                entity.getFileName(),
                entity.getDtime(),
                formatFileSize(entity.getFileSize()),
                DAVRHUB_DOWNLOAD_URL_PREFIX + hashId
        );
    }

    public static String formatFileSize(Long size) {
        if (size == null || size <= 0) return "0 B";
        String[] units = {"B", "KB", "MB", "GB"};
        int unitIndex = (int) (Math.log10(size) / Math.log10(1024));
        double formattedSize = size / Math.pow(1024, unitIndex);
        return String.format("%.2f %s", formattedSize, units[unitIndex]);
    }
}
