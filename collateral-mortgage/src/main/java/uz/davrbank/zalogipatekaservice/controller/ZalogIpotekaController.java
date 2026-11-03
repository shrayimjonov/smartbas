package uz.davrbank.zalogipatekaservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.zalogipatekaservice.aggregate.dto.request.ZalogIpotekaRequestParam;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponseDTO;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponsePaginationDTO;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponseSortDTO;
import uz.davrbank.zalogipatekaservice.service.cases.ZalogIpotekaService;


@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
public class ZalogIpotekaController {

    private final ZalogIpotekaService anInterface;

    @PostMapping("/get/all")
    public ResponseEntity<ResponseDTO> getAll( @RequestParam(value = "startDate", required = false) String startDate,
                                               @RequestParam(value = "endDate", required = false) String endDate,
                                               @RequestParam("cadastreType") Integer cadastreType,
                                               @RequestParam("state") Integer state,
                                               @RequestParam("branch") String branch,
                                               @RequestParam(value = "loanId", required = false) Integer loanId,
                                               @RequestParam(value = "clientId", required = false) String clientId,
                                               @RequestParam(value = "pinfl", required = false) String pinfl,
                                               @RequestParam(value = "clientName", required = false) String clientName,
                                               @RequestParam(value = "cadastreNum", required = false) String cadastreNum)
                                              {
        ZalogIpotekaRequestParam requestParam = new ZalogIpotekaRequestParam();
        requestParam.setStartDate(startDate);
        requestParam.setEndDate(endDate);
        requestParam.setCadastreType(cadastreType);
        requestParam.setState(state);
        requestParam.setBranch(branch);
        requestParam.setLoanId(loanId);
        requestParam.setClientId(clientId);
        requestParam.setPinfl(pinfl);
        requestParam.setClientName(clientName);
        requestParam.setCadastreNum(cadastreNum);
        return ResponseEntity.ok(anInterface.getAll(requestParam));
    }

    @PostMapping("/pagination")
    public ResponseEntity<ResponsePaginationDTO> getPageable(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam("cadastreType") Integer cadastreType,
            @RequestParam("state") Integer state,
            @RequestParam("branch") String branch,
            @RequestParam(value = "loanId", required = false) Integer loanId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "pinfl", required = false) String pinfl,
            @RequestParam(value = "clientName", required = false) String clientName,
            @RequestParam(value = "cadastreNum", required = false) String cadastreNum,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize) {
       ZalogIpotekaRequestParam requestParam = new ZalogIpotekaRequestParam();
       requestParam.setStartDate(startDate);
       requestParam.setEndDate(endDate);
       requestParam.setCadastreType(cadastreType);
       requestParam.setState(state);
       requestParam.setBranch(branch);
       requestParam.setLoanId(loanId);
       requestParam.setClientId(clientId);
       requestParam.setPinfl(pinfl);
       requestParam.setClientName(clientName);
       requestParam.setCadastreNum(cadastreNum);
       requestParam.setPage(pageNumber);
       requestParam.setSize(pageSize);
       return ResponseEntity.ok(anInterface.getPageable(requestParam));
    }

    @PostMapping("/pagination/sort")
    public ResponseEntity<ResponseSortDTO> getPageableSorted(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam("cadastreType") Integer cadastreType,
            @RequestParam("state") Integer state,
            @RequestParam("branch") String branch,
            @RequestParam(value = "loanId", required = false) Integer loanId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "pinfl", required = false) String pinfl,
            @RequestParam(value = "clientName", required = false) String clientName,
            @RequestParam(value = "cadastreNum", required = false) String cadastreNum,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("sort") String sort) {
        ZalogIpotekaRequestParam requestParam = new ZalogIpotekaRequestParam();
        requestParam.setStartDate(startDate);
        requestParam.setEndDate(endDate);
        requestParam.setCadastreType(cadastreType);
        requestParam.setState(state);
        requestParam.setBranch(branch);
        requestParam.setLoanId(loanId);
        requestParam.setClientId(clientId);
        requestParam.setPinfl(pinfl);
        requestParam.setClientName(clientName);
        requestParam.setCadastreNum(cadastreNum);
        requestParam.setPage(pageNumber);
        requestParam.setSize(pageSize);
        requestParam.setSort(sort);
        return ResponseEntity.ok(anInterface.getPageableSorted(requestParam));
    }
}
