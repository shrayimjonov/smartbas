package uz.davrbank.journaltransservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.journaltransservice.aggregate.dto.request.PageableRequestDTO;
import uz.davrbank.journaltransservice.service.interfaces.JournalTransServiceInterface;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
public class JournalTransController {

    private final JournalTransServiceInterface journalTransService;


    @PostMapping("/pagination")
    public ResponseEntity<?> getJournalTransReportPaginated(
            @RequestParam(value = "iStartDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") String iStartDate,
            @RequestParam(value = "iEndDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") String iEndDate,
            @RequestParam(value = "iAccBalKod", required = false) String iAccBalKod,
            @RequestParam(value = "iBranch", required = false) String iBranch,
            @RequestParam("iPageNumber") Integer iPageNumber,
            @RequestParam("iPageSize") Integer iPageSize) {

        PageableRequestDTO dto = PageableRequestDTO.builder()
                .iStartDate(iStartDate)
                .iEndDate(iEndDate)
                .iAccBalKod(iAccBalKod)
                .iBranch(iBranch)
                .iPageNumber(iPageNumber)
                .iPageSize(iPageSize)
                .build();

        return ResponseEntity.ok(journalTransService.getJournalTransReportPaginated(dto));
    }

}
