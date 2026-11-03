package uz.davrbank.userinfobase.feign.elma;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.davrbank.userinfobase.dto.request.elma.ElmaRequestDto;
import uz.davrbank.userinfobase.dto.request.elma.ElmaResponseDto;

@FeignClient(name = "elmaClient", url = "http://127.0.0.1:80", configuration = FeignElmaConfig.class)
public interface ElmaFeignClient {

    @PostMapping("/api/elma/transfer")
    ElmaResponseDto transfer(@RequestBody ElmaRequestDto requestDto);
}
