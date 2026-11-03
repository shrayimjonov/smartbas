package uz.davrbank.yeisvoservice.service.service.interfaces;

import uz.davrbank.yeisvoservice.aggregate.dto.response.report.*;

import java.io.IOException;
import java.util.List;

public interface ReportInterface {

    ResponseDTO<List<?>> getAllReports( ReportAllRequest request);

    ResponseDTO<List<Object>> getPagedReports(ReportPagedRequest request);

    DownloadDTO generateAndUploadExcel( ReportAllRequest request) throws IOException;
}
