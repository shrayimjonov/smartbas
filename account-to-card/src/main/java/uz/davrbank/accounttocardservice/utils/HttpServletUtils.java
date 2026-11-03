package uz.davrbank.accounttocardservice.utils;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.davrbank.accounttocardservice.exception.condition.GeneralErrorException;

import java.util.Objects;


@Slf4j
@Component
public class HttpServletUtils {

    private final NetworkDataService networkDataService;
    private final Gson gson;

    public HttpServletUtils(NetworkDataService networkDataService, Gson gson) {
        this.networkDataService = networkDataService;
        this.gson = gson;
    }

    public static HttpServletRequest getCurrentHttpRequest() {
        return
                ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static String getRequestIPAddress() {
        var requestContext = RequestContextHolder.getRequestAttributes();

        if (requestContext == null)
            throw new GeneralErrorException("Request context requestAttribute is null");

        return
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                        .getRemoteAddr();
    }


    public static HttpServletResponse getCurrentHttpResponse() {
        return
                ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getResponse();
    }

    public static String takeToken() {
        return getCurrentHttpRequest().getHeader("Authorization");
    }


    public void httpRequestLog(HttpServletRequest httpServletRequest) {
        final String clientIP = networkDataService.getClientIPv4Address(httpServletRequest);
        final String clientInfo = networkDataService.getRemoteUserInfo(httpServletRequest);
        log.info("remote address : {} \t\t ", gson.toJson(clientIP));
        log.info("host name : {} \t\t ", gson.toJson(clientInfo));
    }
}
