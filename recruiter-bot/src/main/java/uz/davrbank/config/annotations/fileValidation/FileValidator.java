package uz.davrbank.config.annotations.fileValidation;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileValidator {

    protected static boolean validateFile(MultipartFile file, FileType[] allowedTypes) throws IOException {
        if (file == null || file.isEmpty()) return false;

        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();

        if (filename == null || contentType == null) return false;

        filename = filename.toLowerCase();
        contentType = contentType.toLowerCase();

        try (var input = file.getInputStream()) {
            byte[] header = new byte[8];
            int bytesRead = input.read(header, 0, header.length);

            for (FileType type : allowedTypes) {
                if (!filename.endsWith(type.getExtension()) || !contentType.equalsIgnoreCase(type.getMimeType())) {
                    continue;
                }

                switch (type) {
                    case PDF -> {
                        if (bytesRead >= 5 && header[0] == '%' && header[1] == 'P' &&
                                header[2] == 'D' && header[3] == 'F' && header[4] == '-') {
                            return true;
                        }
                    }
                    case PNG -> {
                        byte[] pngMagic = {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'};
                        if (bytesRead >= pngMagic.length && matchesMagic(header, pngMagic)) return true;
                    }
                    case JPEG, JPG -> {
                        byte[] jpegMagic = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
                        if (bytesRead >= jpegMagic.length && matchesMagic(header, jpegMagic)) return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean matchesMagic(byte[] header, byte[] magic) {
        for (int i = 0; i < magic.length; i++) {
            if (header[i] != magic[i]) return false;
        }
        return true;
    }


}
