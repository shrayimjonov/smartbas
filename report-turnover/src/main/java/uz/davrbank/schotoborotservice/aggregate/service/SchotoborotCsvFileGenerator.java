package uz.davrbank.schotoborotservice.aggregate.service;

import uz.davrbank.schotoborotservice.aggregate.dto.integration.response.SchotoborotGetPageableResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface SchotoborotCsvFileGenerator {

    void writeCsv(OutputStream outputStream, List<SchotoborotGetPageableResponse> data) throws IOException;
}
