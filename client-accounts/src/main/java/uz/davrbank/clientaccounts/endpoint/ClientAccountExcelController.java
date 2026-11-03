package uz.davrbank.clientaccounts.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountRequestDto;
import uz.davrbank.clientaccounts.service.interfaces.ClientAccountReportService;

@RestController
@RequestMapping("/v1/export")
@RequiredArgsConstructor
public class ClientAccountExcelController {

    private final ClientAccountReportService reportService;

    @GetMapping("/excel")
    public ResponseEntity<?> downloadExcel(
            @RequestParam("iDate") String iDate,
            @RequestParam("iBranch") String iBranch,
            @RequestParam("iAccList") String iAccList,
            @RequestParam("iClientType") Integer iClientType,
            @RequestParam("iState") Integer iState,
            @RequestParam("iValType") Integer iValType,
            @RequestParam(value = "iAccount", required = false) String iAccount,
            @RequestParam(value = "iClientId", required = false) String iClientId,
            @RequestHeader(value = "username", defaultValue = "system") String username) {

        ClientAccountRequestDto dto = ClientAccountRequestDto.builder()
                .iDate(iDate)
                .iBranch(iBranch)
                .iAccList(iAccList)
                .iClientType(iClientType)
                .iState(iState)
                .iValType(iValType)
                .iAccount(iAccount)
                .iClientId(iClientId)
                .build();

        return reportService.downloadExcelReport(dto, username);
    }
}