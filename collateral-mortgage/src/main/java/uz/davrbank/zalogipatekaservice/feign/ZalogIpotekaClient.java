package uz.davrbank.zalogipatekaservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponseDTO;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponsePaginationDTO;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponseSortDTO;
import uz.davrbank.zalogipatekaservice.feign.config.GlobalFeignConfiguration;
import uz.davrbank.zalogipatekaservice.feign.config.ZalogIpotekaClientFallback;

@FeignClient(
        name = "zalogipatekahub-service",
        configuration = GlobalFeignConfiguration.class,
        fallback = ZalogIpotekaClientFallback.class)
public interface ZalogIpotekaClient {

    @PostMapping("/hub/api/v1/reports/zalog/ipateka/get/all")
    ResponseDTO getAll(@RequestParam("startDate") String startDate,
                       @RequestParam("endDate") String endDate,
                       @RequestParam("cadastreType") Integer cadastreType,
                       @RequestParam("state") Integer state,
                       @RequestParam("branch") String branch,
                       @RequestParam(value = "loanId", required = false) Integer loanId,
                       @RequestParam(value = "clientId", required = false) String clientId,
                       @RequestParam(value = "pinfl", required = false) String pinfl,
                       @RequestParam(value = "clientName", required = false) String clientName,
                       @RequestParam(value = "cadastreNum", required = false) String cadastreNum);

    @PostMapping("/hub/api/v1/reports/zalog/ipateka/page")
    ResponsePaginationDTO getPageable(@RequestParam("startDate") String startDate,
                                      @RequestParam("endDate") String endDate,
                                      @RequestParam("cadastreType") Integer cadastreType,
                                      @RequestParam("state") Integer state,
                                      @RequestParam("pageNumber") Integer pageNumber,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam("branch") String branch,
                                      @RequestParam(value = "loanId", required = false) Integer loanId,
                                      @RequestParam(value = "clientId", required = false) String clientId,
                                      @RequestParam(value = "pinfl", required = false) String pinfl,
                                      @RequestParam(value = "clientName", required = false) String clientName,
                                      @RequestParam(value = "cadastreNum", required = false) String cadastreNum);

    @PostMapping("/hub/api/v1/reports/zalog/ipateka/page/sort")
    ResponseSortDTO getPageableSorted(@RequestParam("startDate") String startDate,
                                      @RequestParam("endDate") String endDate,
                                      @RequestParam("cadastreType") Integer cadastreType,
                                      @RequestParam("state") Integer state,
                                      @RequestParam("pageNumber") Integer pageNumber,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam("sort") String sort,
                                      @RequestParam("branch") String branch,
                                      @RequestParam(value = "loanId", required = false) Integer loanId,
                                      @RequestParam(value = "clientId", required = false) String clientId,
                                      @RequestParam(value = "pinfl", required = false) String pinfl,
                                      @RequestParam(value = "clientName", required = false) String clientName,
                                      @RequestParam(value = "cadastreNum", required = false) String cadastreNum);
}

