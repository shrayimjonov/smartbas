package uz.davrbank.creditaccountsservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditAccountRequestDTO;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditRequestPageable;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditRequestSorted;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponseDTO;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponsePaginationDTO;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponseSortDTO;

@FeignClient(
        name = "creditaccountshub-service",
        configuration = GlobalFeignConfiguration.class,
        fallback = CreditAccountClientFallback.class
)
public interface CreditAccountClient {

    @PostMapping("/hub/api/credit/account/get/all")
    ResponseDTO getAllAccountCard(
            @RequestBody CreditAccountRequestDTO request
    );

    @PostMapping("/hub/api/credit/account/get/all/page")
    ResponsePaginationDTO getAllPageable(
            @RequestBody CreditRequestPageable request
    );

    @PostMapping("/hub/api/credit/account/get/all/page/sort")
    ResponseSortDTO getAllPageableSorted(
            @RequestBody CreditRequestSorted request
    );
}
