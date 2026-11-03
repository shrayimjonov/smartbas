package uz.davrbank.userinfobase.feign.dochub;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import uz.davrbank.userinfobase.dto.request.dochub.DochubRequest;
import uz.davrbank.userinfobase.dto.response.dochub.DochubResponse;
import uz.davrbank.userinfobase.utils.Endpoints;

@FeignClient(
        name = "Dochub",
        url = "https://internal.example.com",
        configuration = FeignDochubConfig.class
)
public interface DochubTransferFeign {
    @PostMapping("/api")
    DochubResponse getDochubTransfer(DochubRequest request);
}
