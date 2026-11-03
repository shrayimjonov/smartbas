package uz.davrbank.schotoborotservice.aggregate.service;


import uz.davrbank.schotoborotservice.aggregate.filter.SchotoborotFilter;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface MinioService {

    String createExcel(SchotoborotFilter filter, byte[] bytes);

    Optional<byte[]> getCsvZipIfExists(String folderName);

    void createCsvZip(String folderName, byte[] bytes);

    void deleteCsvZipIfExists(String folderName);

    void uploadCsvObject(String folderName, String fileName, byte[] bytes);

    List<String> listCsvObjects(String folderName);

    InputStream getCsvObjectStream(String objectPath);

    void deleteCsvObjects(String folderName);

    Optional<byte[]> getExcelZipIfExists(String folderName);

    void createExcelZip(String folderName, byte[] bytes);

    void deleteExcelZipIfExists(String folderName);

    void uploadExcelObject(String folderName, String fileName, byte[] bytes);

    List<String> listExcelObjects(String folderName);

    InputStream getExcelObjectStream(String objectPath);

    void deleteExcelObjects(String folderName);
}
