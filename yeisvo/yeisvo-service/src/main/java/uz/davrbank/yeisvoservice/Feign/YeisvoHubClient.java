package uz.davrbank.yeisvoservice.Feign;


import uz.davrbank.yeisvoservice.Feign.config.GlobalFeignConfiguration;
import uz.davrbank.yeisvoservice.Feign.config.YeisvoClientFallBack;
import uz.davrbank.yeisvoservice.aggregate.dto.response.ResponseInnDTO;
import uz.davrbank.yeisvoservice.aggregate.dto.response.report.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "yeisvohub-service",
        configuration = GlobalFeignConfiguration.class,
        fallback = YeisvoClientFallBack.class)
public interface YeisvoHubClient {


    @PostMapping("/hub/api/v1/juridic/get/all")
    ResponseDTO<List<ResponseInnDTO>> getAll(@RequestParam("bankId") String bankId,
                                             @RequestParam("active") String active
    );

    @PostMapping("/hub/api/v1/reports/yeisvo/get/all")
    ResponseDTO<List<?>> getAllReports(
            @RequestParam("transType") Integer transType,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    );

    @PostMapping("/hub/api/v1/reports/yeisvo/page")
    ResponseDTO<List<Object>> getPagedReports(
            @RequestParam("transType") Integer transType,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize
    );


}


