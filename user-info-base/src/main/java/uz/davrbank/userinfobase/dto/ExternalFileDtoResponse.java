package uz.davrbank.userinfobase.dto;

import uz.davrbank.userinfobase.entity.DavrhubFile;
import uz.davrbank.userinfobase.entity.ExternalFile;
import uz.davrbank.userinfobase.service.HashIdService;

import java.util.Optional;

public record ExternalFileDtoResponse(

        Long id,
        String fileName,
        String fileSize,
        String source,
        String nameDoc,
        String fileUrl

) {
    private static final String EXTERNAL_FILE_DOWNLOAD_URL_PREFIX =
            "https://internal.example.com/back/api/external-files/download/";
    private static final String DAVRHUB_DOWNLOAD_URL_PREFIX =
            "https://internal.example.com/back/api/davrhub/download/";

    public static ExternalFileDtoResponse fromExternalFile(
            ExternalFile entity,
            HashIdService hashIdService,
            int counter
    ) {

        String hashId = hashIdService.encode(entity.getId());

        return new ExternalFileDtoResponse(
                entity.getId(),
                entity.getSource() + "_" + entity.getLoanId() + "_" + entity.getBranchId() + "_" + counter,
                formatFileSize(entity.getFileSize()),
                entity.getSource().name(),
                Optional.ofNullable(entity.getNameDoc()).orElseGet(entity::getFileName),
                EXTERNAL_FILE_DOWNLOAD_URL_PREFIX + hashId
        );
    }

    public static ExternalFileDtoResponse fromDavrhubFile(
            DavrhubFile entity,
            HashIdService hashIdService
    ) {
        String hashId = hashIdService.encode(entity.getId());

        return new ExternalFileDtoResponse(
                entity.getId(),
                entity.getFileName(),
                formatFileSize(entity.getFileSize()),
                "NCI",
                entity.getNameDoc(),
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
