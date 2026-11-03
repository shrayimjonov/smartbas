package uz.davrbank.config.annotations.fileValidation;


import lombok.Getter;

@Getter
public enum FileType {
    PDF(".pdf", "application/pdf"),
    PNG(".png", "image/png"),
    JPEG(".jpg", "image/jpeg"),
    JPG(".jpeg", "image/jpeg");

    private final String extension;
    private final String mimeType;

    FileType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

}

