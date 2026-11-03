package uz.davrbank.creditaccountsservice.feign;

import org.springframework.stereotype.Component;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditAccountRequestDTO;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditRequestPageable;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditRequestSorted;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponseDTO;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponsePaginationDTO;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponseSortDTO;


import java.util.ArrayList;
@Component
public class CreditAccountClientFallback implements CreditAccountClient {

    @Override
    public ResponseDTO getAllAccountCard(CreditAccountRequestDTO creditAccountRequestDTO) {

        ResponseDTO response = new ResponseDTO();
        response.setError("Hub service unavailable. Fallback response returned.");
        response.setData(null);
        return response;
    }

    @Override
    public ResponsePaginationDTO getAllPageable(CreditRequestPageable creditRequestPageable) {

        ResponsePaginationDTO response = new ResponsePaginationDTO();
        response.setError("Hub service unavailable. Fallback response returned.");
        response.setData(new ArrayList<>());
        return response;
    }

    @Override
    public ResponseSortDTO getAllPageableSorted(CreditRequestSorted creditRequestSorted) {

        ResponseSortDTO response = new ResponseSortDTO();
        response.setError("Hub service unavailable. Fallback response returned.");
        response.setData(new ArrayList<>());
        return response;
    }
}
