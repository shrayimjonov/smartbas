package uz.davrbank.zalogautoservice.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.zalogautoservice.aggregate.dto.*;
import uz.davrbank.zalogautoservice.service.interfaces.AutoServiceInterface;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
public class AutoController {

    private final AutoServiceInterface autoService;

    @PostMapping("/get/all")
    public ResponseEntity<AutoResponseDto> getAutoZalogReport(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") String startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") String endDate,
            @RequestParam("state") Integer state,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "loanId", required = false) Integer loanId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "pinfl", required = false) String pinfl,
            @RequestParam(value = "clientName", required = false) String clientName,
            @RequestParam(value = "stateNumber", required = false) String stateNumber,
            @RequestParam(value = "address", required = false) String address) {

        AutoRequestDto dto = AutoRequestDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .state(state)
                .branch(branch)
                .loanId(loanId)
                .clientId(clientId)
                .pinfl(pinfl)
                .clientName(clientName)
                .stateNumber(stateNumber)
                .address(address)
                .build();

        return ResponseEntity.ok(autoService.getAutoZalogReport(dto));
    }

    @PostMapping("/pagination")
    public ResponseEntity<?> getAutoZalogReportPaginated(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") String startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") String endDate,
            @RequestParam("state") Integer state,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "loanId", required = false) Integer loanId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "pinfl", required = false) String pinfl,
            @RequestParam(value = "clientName", required = false) String clientName,
            @RequestParam(value = "stateNumber", required = false) String stateNumber,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize) {

        AutoRequestDto dto = AutoRequestDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .state(state)
                .branch(branch)
                .loanId(loanId)
                .clientId(clientId)
                .pinfl(pinfl)
                .clientName(clientName)
                .stateNumber(stateNumber)
                .address(address)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();

        return ResponseEntity.ok(autoService.getAutoZalogReportPaginated(dto));
    }

    @PostMapping("/pagination/sort")
    public ResponseEntity<?> getAutoZalogReportPaginatedSort(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") String startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") String endDate,
            @RequestParam("state") Integer state,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "loanId", required = false) Integer loanId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "pinfl", required = false) String pinfl,
            @RequestParam(value = "clientName", required = false) String clientName,
            @RequestParam(value = "stateNumber", required = false) String stateNumber,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sort") String sort) {

        AutoPaginatedSortRequestDto dto = AutoPaginatedSortRequestDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .state(state)
                .branch(branch)
                .loanId(loanId)
                .clientId(clientId)
                .pinfl(pinfl)
                .clientName(clientName)
                .stateNumber(stateNumber)
                .address(address)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sort(sort)
                .build();

        return ResponseEntity.ok(autoService.getAutoZalogReportPaginatedSort(dto));
    }
}