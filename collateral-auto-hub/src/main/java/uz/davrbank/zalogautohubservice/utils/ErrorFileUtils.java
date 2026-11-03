//package uz.davrbank.zalogautohubservice.utils;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.time.LocalDate;
//
//import static io.micrometer.common.util.StringUtils.truncate;
//
///**
// */
//@Service
//public class ErrorFileUtils {
//
//    private static final Logger LOG = LoggerFactory.getLogger(ErrorFileUtils.class);
//    private final HumoExptErrorRepository humoExptErrorRepository;
//
//    public ErrorFileUtils(HumoExptErrorRepository humoExptErrorRepository) {
//        this.humoExptErrorRepository = humoExptErrorRepository;
//    }
//
//    public void createErrorDirectory() {
//        LOG.info("createErrorDirectory: Creating error directory {}", DIRECTORY_PATH_HUMO_ERROR);
//
//        String folderName = "humoerror";
//        File errorDir = new File(DIRECTORY_PATH_HUMO_ERROR, folderName);
//
//        // Validate path
//        if (DIRECTORY_PATH_HUMO_ERROR == null || DIRECTORY_PATH_HUMO_ERROR.trim().isEmpty()) {
//            LOG.error("Error directory path is null or empty");
//            throw new IllegalStateException("Error directory path is null or empty");
//        }
//
//        // Check if path exists as a file
//        if (errorDir.exists() && !errorDir.isDirectory()) {
//            LOG.error("Path exists but is not a directory: {}", DIRECTORY_PATH_HUMO_ERROR);
//            if (errorDir.delete()) {
//                LOG.info("Deleted existing file at: {}", DIRECTORY_PATH_HUMO_ERROR);
//            } else {
//                LOG.error("Failed to delete existing file at: {}", DIRECTORY_PATH_HUMO_ERROR);
//                throw new IllegalStateException("Failed to delete existing file: " + DIRECTORY_PATH_HUMO_ERROR);
//            }
//        }
//
//        if (!errorDir.exists()) {
//            try {
//                if (errorDir.mkdirs()) {
//                    LOG.info("Created error directory: {}", DIRECTORY_PATH_HUMO_ERROR);
//                } else {
//                    LOG.error("Failed to create error directory: {}", DIRECTORY_PATH_HUMO_ERROR);
//                    throw new IllegalStateException("Failed to create error directory: " + DIRECTORY_PATH_HUMO_ERROR);
//                }
//            } catch (SecurityException e) {
//                LOG.error("Permission denied while creating directory: {}", DIRECTORY_PATH_HUMO_ERROR, e);
//                throw new IllegalStateException("Permission denied: " + DIRECTORY_PATH_HUMO_ERROR, e);
//            }
//        }
//    }
//
//    public void moveFileToErrorDirectory(File file, HumoExptFileError error) {
//        Path sourcePath = file.toPath();
//        Path targetPath = Paths.get(DIRECTORY_PATH_HUMO_ERROR, file.getName());
//
//        if (!Files.exists(sourcePath)) {
//            LOG.warn("Source file does not exist: {}", sourcePath);
//            return;
//        }
//
//        try {
//            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
//            LOG.info("Moved file to error directory: fileName={}, targetPath={}", file.getName(), targetPath);
//        } catch (IOException e) {
//            LOG.error("Failed to move file to error directory: fileName={}, targetPath={}, error={}",
//                    file.getName(), targetPath, e.getMessage(), e);
//
//            HumoExptFileError moveError = HumoExptFileError.builder()
//                    .fileName(file.getName())
//                    .fileId(error != null ? error.getFileId() : "N/A")
//                    .fileDate(LocalDate.now().toString())
//                    .errorMessage("Failed to move file to error directory: " + e.getMessage())
//                    .errorCode("FILE_MOVE_ERROR")
//                    .build();
//
//            saveToErrorToDatabase(moveError);
//        }
//    }
//
//    private void saveToErrorToDatabase(HumoExptFileError error) {
//        try {
//            humoExptErrorRepository.save(error);
//            LOG.info("Saved error to DB: {}", error.getFileName());
//        } catch (Exception e) {
//            LOG.error("Failed to save HumoExptFileError to DB: {}", e.getMessage(), e);
//        }
//    }
//
//    public HumoExptFileError validateFile(File file) {
//        LOG.info("validateFile: Validating file {}", file);
//        String fileName = file.getName();
//        if (!file.exists() || !file.isFile()) {
//            return createError(fileName, "N/A", "Invalid file: " + fileName, "INVALID_FILE");
//        }
//        return null;
//    }
//
//    public HumoExptFileError createError(String fileName, String fileId, String errorMessage, String errorCode) {
//        return HumoExptFileError.builder()
//                .fileName(fileName)
//                .fileId(fileId)
//                .fileDate(LocalDate.now().toString())
//                .errorMessage(truncate(errorMessage, 255))
//                .errorCode(errorCode)
//                .condition(false)
//                .build();
//    }
//
//    public void saveAndMoveFile(File file, HumoExptFileError error) {
//        try {
//            humoExptErrorRepository.save(error);
//            LOG.info("Saved error to DB for file: {}", file.getName());
//        } catch (Exception e) {
//            LOG.error("Failed to save HumoExptFileError to DB for file {}: {}", file.getName(), e.getMessage(), e);
//        }
//        moveFileToErrorDirectory(file, error);
//    }
//
//    public HumoExptFileError validateFileReadability(File file) {
//        String fileName = file.getName();
//        if (!file.exists()) {
//            return createError(fileName, "N/A", "File does not exist: " + fileName,
//                    "FILE_NOT_FOUND");
//        }
//        if (!file.canRead()) {
//            return createError(fileName, "N/A", "Cannot read file (permission denied): " + fileName,
//                    "PERMISSION_DENIED");
//        }
//        if (file.length() == 0) {
//            return createError(fileName, "N/A", "File is empty: " + fileName, "EMPTY_FILE");
//        }
//        return null;
//    }
//}
