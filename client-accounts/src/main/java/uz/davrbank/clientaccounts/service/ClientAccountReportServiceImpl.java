package uz.davrbank.clientaccounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.davrbank.clientaccounts.aggregate.cloud.service.ClientAccountMinioService;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountDataDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountPaginatedResponseDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountRequestDto;
import uz.davrbank.clientaccounts.aggregate.dto.ErrorResponseDto;
import uz.davrbank.clientaccounts.service.interfaces.ClientAccountReportService;
import uz.davrbank.clientaccounts.service.interfaces.ClientAccountService;
import uz.davrbank.clientaccounts.utils.ClientAccountExcelStyles;

import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientAccountReportServiceImpl implements ClientAccountReportService {

    private static final Logger logger = LoggerFactory.getLogger(ClientAccountReportServiceImpl.class);

    private final ClientAccountService clientAccountService;
    private final ClientAccountMinioService minioService;

    private static final int PAGE_SIZE = 500_000;
    private static final int MAX_ROWS_PER_FILE = 500_000;

    @Override
    public ResponseEntity<?> downloadExcelReport(ClientAccountRequestDto dto, String username) {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        long startTime = System.currentTimeMillis();

        logger.info("[REQ-{}] Excel export STARTED | user: {} | iDate: {} | iBranch: {}",
                requestId, username, dto.getIDate(), dto.getIBranch());

        try {
            validateForExcel(dto);

            byte[] resultFileBytes = generateExcelStreaming(dto, username, requestId);
            long totalRows = getTotalCount(dto);
            boolean isZip = totalRows > MAX_ROWS_PER_FILE;

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"));
            String datePart = now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            String timePart = now.format(DateTimeFormatter.ofPattern("HH-mm"));
            String extension = isZip ? "zip" : "xlsx";

            String fileName = String.format("ClientAccounts_%s_%s.%s", datePart, timePart, extension);

            long generationSeconds = (System.currentTimeMillis() - startTime) / 1000;
            logger.info("[REQ-{}] File generated in {}s → {} rows → {}",
                    requestId, generationSeconds, totalRows, fileName);

            ResponseEntity<ByteArrayResource> response = buildResponse(resultFileBytes, fileName, isZip);

            CompletableFuture.runAsync(() -> {
                try {
                    minioService.uploadExcelToMinio(resultFileBytes, dto.getIDate(), dto.getIBranch(), dto.getIAccList());
                    logger.info("[REQ-{}] File uploaded to MinIO → {}", requestId, fileName);
                } catch (Exception e) {
                    logger.error("[REQ-{}] MinIO upload failed", requestId, e);
                }
            });

            return response;

        } catch (Exception e) {
            logger.error("[REQ-{}] Excel generation failed", requestId, e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponseDto(500, "Report generation failed: " + e.getMessage()));
        }
    }

    // heap tozalanadi, 500k chunk, GC chaqiriladi
    private byte[] generateExcelStreaming(ClientAccountRequestDto dto, String username, String requestId) throws Exception {
        long totalElements = getTotalCount(dto);
        if (totalElements == 0) {
            throw new IllegalStateException("No data found");
        }

        if (totalElements <= MAX_ROWS_PER_FILE) {
            List<ClientAccountDataDto> chunk = fetchPage(dto, 1);
            byte[] excel = generateSingleExcel(chunk, username);
            chunk.clear(); // heap tozalash
            return excel;
        }

        ByteArrayOutputStream zipOut = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(zipOut)) {
            int totalPages = (int) Math.ceil((double) totalElements / PAGE_SIZE);

            for (int page = 1; page <= totalPages; page++) {
                logger.info("[REQ-{}] Generating part {}/{}", requestId, page, totalPages);

                List<ClientAccountDataDto> chunk = fetchPage(dto, page);
                byte[] excelBytes = generateSingleExcel(chunk, username);

                zos.putNextEntry(new ZipEntry("ClientAccounts_part" + page + ".xlsx"));
                zos.write(excelBytes);
                zos.closeEntry();

                // Heap tozalash — har bir chunkdan keyin!
                chunk.clear();
                chunk = null;

                if (page % 2 == 0) System.gc();
            }
        }
        return zipOut.toByteArray();
    }

    private List<ClientAccountDataDto> fetchPage(ClientAccountRequestDto baseDto, int pageNumber) {
        ClientAccountRequestDto pageDto = ClientAccountRequestDto.builder()
                .iDate(baseDto.getIDate())
                .iBranch(baseDto.getIBranch())
                .iAccList(baseDto.getIAccList())
                .iClientType(baseDto.getIClientType())
                .iState(baseDto.getIState())
                .iValType(baseDto.getIValType())
                .iAccount(baseDto.getIAccount())
                .iClientId(baseDto.getIClientId())
                .iPageNumber(pageNumber)
                .iPageSize(PAGE_SIZE)
                .iSort(baseDto.getISort())
                .build();

        ClientAccountPaginatedResponseDto response = (baseDto.getISort() != null && !baseDto.getISort().isBlank())
                ? clientAccountService.getAccountsPageSort(pageDto)
                : clientAccountService.getAccountsPage(pageDto);

        if ("error".equalsIgnoreCase(response.getStatus())) {
            throw new RuntimeException("Hub error on page " + pageNumber);
        }

        return response.getData() != null ? response.getData() : List.of();
    }

    private long getTotalCount(ClientAccountRequestDto dto) {
        ClientAccountRequestDto countDto = ClientAccountRequestDto.builder()
                .iDate(dto.getIDate())
                .iBranch(dto.getIBranch())
                .iAccList(dto.getIAccList())
                .iClientType(dto.getIClientType())
                .iState(dto.getIState())
                .iValType(dto.getIValType())
                .iAccount(dto.getIAccount())
                .iClientId(dto.getIClientId())
                .iPageNumber(1)
                .iPageSize(1)
                .iSort(dto.getISort())
                .build();

        ClientAccountPaginatedResponseDto response = (dto.getISort() != null && !dto.getISort().isBlank())
                ? clientAccountService.getAccountsPageSort(countDto)
                : clientAccountService.getAccountsPage(countDto);

        return response.getTotalElements() != null ? response.getTotalElements() : 0;
    }

    private byte[] generateSingleExcel(List<ClientAccountDataDto> data, String username) throws Exception {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            workbook.setCompressTempFiles(true);
            SXSSFSheet sheet = workbook.createSheet("Client Accounts");

            CellStyle headerStyle = ClientAccountExcelStyles.headerStyle(workbook);
            CellStyle dataStyle = ClientAccountExcelStyles.dataStyle(workbook);
            CellStyle amountStyle = ClientAccountExcelStyles.amountStyle(workbook);
            CellStyle boldStyle = ClientAccountExcelStyles.boldStyle(workbook);

            int rowIdx = 0;
            Row row = sheet.createRow(rowIdx++);
            ClientAccountExcelStyles.setCellValue(row, 0, "Отчет:", boldStyle);
            ClientAccountExcelStyles.setCellValue(row, 1, "Отчёт о клиентских счетах", dataStyle);

            row = sheet.createRow(rowIdx++);
            ClientAccountExcelStyles.setCellValue(row, 0, "Отделение:", boldStyle);
            ClientAccountExcelStyles.setCellValue(row, 1, "ЧАКБ «Davr bank» — Департамент ИТ, Отдел интеграции", dataStyle);

            row = sheet.createRow(rowIdx++);
            ClientAccountExcelStyles.setCellValue(row, 0, "Загружено:", boldStyle);
            ClientAccountExcelStyles.setCellValue(row, 1, username != null && !username.isBlank() ? username : "system", dataStyle);

            String now = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"))
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            row = sheet.createRow(rowIdx++);
            ClientAccountExcelStyles.setCellValue(row, 0, "Дата:", boldStyle);
            ClientAccountExcelStyles.setCellValue(row, 1, now, dataStyle);

            rowIdx += 2;

            String[] headers = {
                    "Филиал", "Идентификатор клиента", "Имя владельца счёта", "ПИНФЛ/ИНН",
                    "Номер счёта", "Сальдо (номинал)", "Сальдо (эквивалент)", "Статус"
            };

            Row headerRow = sheet.createRow(rowIdx++);
            headerRow.setHeightInPoints(35);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            for (ClientAccountDataDto item : data) {
                Row dataRow = sheet.createRow(rowIdx++);
                int col = 0;
                ClientAccountExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getBranch()), dataStyle);
                ClientAccountExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getIdClient()), dataStyle);
                ClientAccountExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getAccName()), dataStyle);
                ClientAccountExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getPinflInn()), dataStyle);
                ClientAccountExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getAccount()), dataStyle);
                ClientAccountExcelStyles.setCellValue(dataRow, col++, item.getSaldoNom(), amountStyle);
                ClientAccountExcelStyles.setCellValue(dataRow, col++, item.getSaldoEqv(), amountStyle);
                ClientAccountExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getState()), dataStyle);
            }

            sheet.setColumnWidth(0, 15 * 256);
            sheet.setColumnWidth(1, 30 * 256);
            sheet.setColumnWidth(2, 45 * 256);
            sheet.setColumnWidth(3, 20 * 256);
            sheet.setColumnWidth(4, 28 * 256);
            sheet.setColumnWidth(5, 22 * 256);
            sheet.setColumnWidth(6, 22 * 256);
            sheet.setColumnWidth(7, 18 * 256);

            workbook.write(out);
            workbook.dispose();
            return out.toByteArray();
        }
    }

    private void validateForExcel(ClientAccountRequestDto dto) {
        if (dto.getIDate() == null || dto.getIDate().trim().isEmpty())
            throw new IllegalArgumentException("iDate is required");
        if (dto.getIBranch() == null || dto.getIBranch().trim().isEmpty())
            throw new IllegalArgumentException("iBranch is required");
        if (dto.getIAccList() == null || dto.getIAccList().trim().isEmpty())
            throw new IllegalArgumentException("iAccList is required");
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    private ResponseEntity<ByteArrayResource> buildResponse(byte[] bytes, String fileName, boolean isZip) {
        HttpHeaders headers = new HttpHeaders();

        String disposition = "attachment; filename*=%s".formatted(fileName);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, disposition);

        if (isZip) {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        } else {
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        }

        headers.setCacheControl("no-cache, no-store, must-revalidate");
        headers.setPragma("no-cache");
        headers.setExpires(0);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
    }
}