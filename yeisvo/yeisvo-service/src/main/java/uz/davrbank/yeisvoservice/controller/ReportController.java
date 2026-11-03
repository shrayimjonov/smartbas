package uz.davrbank.yeisvoservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.yeisvoservice.aggregate.dto.response.report.*;
import uz.davrbank.yeisvoservice.service.service.sevices.ReportService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/swift")
@RequiredArgsConstructor
public class ReportController {

        private final ReportService reportService;

        @PostMapping("/get/all")
        public ResponseDTO<List<?>> getAllReports(@RequestBody ReportAllRequest request) {
            return reportService.getAllReports(request);
        }

        @PostMapping("/get/page")
        public ResponseDTO<List<Object>> getPagedReports(@RequestBody ReportPagedRequest request) {
            return reportService.getPagedReports(request);
        }

        @PostMapping("/download/excel")
        public DownloadDTO generateAndUploadExcel(@RequestBody ReportAllRequest request) throws IOException {
            return reportService.generateAndUploadExcel(request);
        }
}
