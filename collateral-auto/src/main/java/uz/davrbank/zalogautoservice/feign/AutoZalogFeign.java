package uz.davrbank.zalogautoservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.davrbank.zalogautoservice.aggregate.dto.AutoPaginatedResponseDto;
import uz.davrbank.zalogautoservice.aggregate.dto.AutoResponseDto;
import uz.davrbank.zalogautoservice.feign.config.GlobalFeignConfiguration;
import uz.davrbank.zalogautoservice.feign.config.ZalogAutoClientFallback;

@FeignClient(
        name = "zalogautohub-service",
        configuration = GlobalFeignConfiguration.class,
        fallback = ZalogAutoClientFallback.class
)
public interface AutoZalogFeign {

    @PostMapping("/hub/api/v1/report/zalog/auto/get/all")
    AutoResponseDto getAutoZalogReport(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam("state") Integer state,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "loanId", required = false) Integer loanId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "pinfl", required = false) String pinfl,
            @RequestParam(value = "clientName", required = false) String clientName,
            @RequestParam(value = "stateNumber", required = false) String stateNumber,
            @RequestParam(value = "address", required = false) String address
    );

    @PostMapping("/hub/api/v1/report/zalog/auto/get/all/page")
    AutoPaginatedResponseDto getAutoZalogReportPaginated(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam("state") Integer state,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "loanId", required = false) Integer loanId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "pinfl", required = false) String pinfl,
            @RequestParam(value = "clientName", required = false) String clientName,
            @RequestParam(value = "stateNumber", required = false) String stateNumber,
            @RequestParam(value = "address", required = false) String address
    );

    @PostMapping("/hub/api/v1/report/zalog/auto/get/all/page/sort")
    AutoPaginatedResponseDto getAutoZalogReportPaginatedSort(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam("state") Integer state,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sort") String sort,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "loanId", required = false) Integer loanId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "pinfl", required = false) String pinfl,
            @RequestParam(value = "clientName", required = false) String clientName,
            @RequestParam(value = "stateNumber", required = false) String stateNumber,
            @RequestParam(value = "address", required = false) String address
    );
}