package uz.davrbank.yeisvoservice.Feign.config;

import uz.davrbank.yeisvoservice.Feign.YeisvoHubClient;
import uz.davrbank.yeisvoservice.aggregate.dto.response.ResponseInnDTO;
import uz.davrbank.yeisvoservice.aggregate.dto.response.report.ResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class YeisvoClientFallBack implements YeisvoHubClient {

    @Override
    public ResponseDTO<List<ResponseInnDTO>> getAll(String bankId, String active) {
        ResponseDTO<List<ResponseInnDTO>> response = new ResponseDTO<>();
        response.setError("Hub service unavailable. Fallback response returned.");
        response.setData(null);
        return response;

    }



    @Override
    public ResponseDTO<List<?>> getAllReports(Integer transType, String startDate, String endDate) {
        ResponseDTO<List<?>> response =  new ResponseDTO<>();
        response.setError("Hub service unavailable (getAllReports). Fallback response returned.");
        response.setData(Collections.emptyList());
        return response;
    }

    @Override
    public ResponseDTO<List<Object>> getPagedReports(Integer transType, String startDate, String endDate, Integer pageNumber, Integer pageSize) {
        ResponseDTO<List<Object>> response = new ResponseDTO<>();
        response.setError("Hub service unavailable (getPagedReports). Fallback response returned.");
        response.setData(Collections.emptyList());
        return response;
    }


    
}
