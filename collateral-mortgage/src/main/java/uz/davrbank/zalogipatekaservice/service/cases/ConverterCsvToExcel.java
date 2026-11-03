package uz.davrbank.zalogipatekaservice.service.cases;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;

public interface ConverterCsvToExcel {
    ResponseEntity<ByteArrayResource> convertCSVToExcel(InputStream csvFileInputStream);
}
