package uz.davrbank.zalogipatekaservice.feign.config;

import org.springframework.stereotype.Component;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponseDTO;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponsePaginationDTO;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponseSortDTO;
import uz.davrbank.zalogipatekaservice.feign.ZalogIpotekaClient;

import java.util.ArrayList;

@Component
public class ZalogIpotekaClientFallback implements ZalogIpotekaClient {

    @Override
    public ResponseDTO getAll(String startDate,
                              String endDate,
                              Integer cadastreType,
                              Integer state,
                              String branch,
                              Integer loanId,
                              String clientId,
                              String pinfl,
                              String clientName,
                              String cadastreNum) {

        ResponseDTO response = new ResponseDTO();
        response.setError("Hub service unavailable. Fallback response returned.");
        response.setData(null);
        return response;
    }

    @Override
    public ResponsePaginationDTO getPageable(String startDate,
                                             String endDate,
                                             Integer cadastreType,
                                             Integer state,
                                             Integer pageNumber,
                                             Integer pageSize,
                                             String branch,
                                             Integer loanId,
                                             String clientId,
                                             String pinfl,
                                             String clientName,
                                             String cadastreNum) {

        ResponsePaginationDTO response = new ResponsePaginationDTO();
        response.setError("Hub service unavailable. Fallback response returned.");
        response.setData(new ArrayList<>());
        return response;
    }

    @Override
    public ResponseSortDTO getPageableSorted(String startDate,
                                             String endDate,
                                             Integer cadastreType,
                                             Integer state,
                                             Integer pageNumber,
                                             Integer pageSize,
                                             String sort,
                                             String branch,
                                             Integer loanId,
                                             String clientId,
                                             String pinfl,
                                             String clientName,
                                             String cadastreNum) {

        ResponseSortDTO response = new ResponseSortDTO();
        response.setError("Hub service unavailable. Fallback response returned.");
        response.setData(new ArrayList<>());
        return response;
    }
}
