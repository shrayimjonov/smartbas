package uz.davrbank.journaltransservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.davrbank.journaltransservice.aggregate.dto.response.PaginatedResponseDTO;
import uz.davrbank.journaltransservice.feign.config.GlobalFeignConfiguration;


@FeignClient(
        name = "journaltranshub-service",
        configuration = GlobalFeignConfiguration.class
)
public interface JournalTransFeign {

    @PostMapping("/hub/api/journal/trans/get/all/page")
    PaginatedResponseDTO getJournalTransReportPaginated(
            @RequestParam(value = "iStartDate", required = false) String iStartDate,
            @RequestParam(value = "iEndDate", required = false) String iEndDate,
            @RequestParam(value = "iAccBalKod", required = false) String iAccBalKod,
            @RequestParam(value = "iBranch", required = false) String iBranch,
            @RequestParam("iPageNumber") Integer iPageNumber,
            @RequestParam("iPageSize") Integer iPageSize
    );
}
