package uz.davrbank.educontrolservice.feign.security;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import uz.davrbank.educontrolservice.aggregate.dto.auth.LoginRequest;
import uz.davrbank.educontrolservice.aggregate.dto.auth.Token;
import uz.davrbank.educontrolservice.aggregate.dto.auth.UserInfoResponse;
import uz.davrbank.educontrolservice.feign.config.EduexamFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "security-service", contextId = "user", configuration = EduexamFeignServiceConfiguration.class)
public interface UserInterfaceFeign {

    @PostMapping("/api/v1/security/user/login")
    Token login(@RequestBody LoginRequest loginRequest);

    @PostMapping("/api/v1/security/user/info")
    UserInfoResponse getUserInfo(@RequestParam String token);

    @PostMapping("/api/v1/security/user/info")
    UserInfoResponse refreshToken(@RequestParam String token);

    @PostMapping("/api/v1/security/user/info")
    UserInfoResponse logout(@RequestParam String token);


    @GetMapping("/api/v1/security/user/all")
    List<Object> getAllUsers();
}
