package uz.davrbank.educontrolservice.aggregate.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;

@Component
@RequiredArgsConstructor
public class GlobalFeignInterceptor implements RequestInterceptor {

    private final JwtTokenService jwtTokenService;

    @Override
    public void apply(RequestTemplate template) {
//        Long deptId = jwtTokenService.getDepartmentId();
/*        if (deptId != null) {
            // Adds "X-Department-Id: 5" to the request header
            template.header("X-Department-Id", String.valueOf(deptId));
        }*/
    }
}