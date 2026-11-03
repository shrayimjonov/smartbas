package soap.humo_gateway.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soap.humo_gateway.dto.enums.SubSystem;
import soap.humo_gateway.entity.LogProtocol;
import soap.humo_gateway.entity.RequestProtocol;
import soap.humo_gateway.repository.LogRepository;
import soap.humo_gateway.service.LogService;
import soap.humo_gateway.service.RequestService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogImpl implements LogService {
    private final LogRepository repository;
    private final RequestService requestService;


    @Override
    @Transactional
    public void createLog(ServletRequest servlet, SubSystem subSystem, String body) {
        HttpServletRequest request = (HttpServletRequest) servlet;

        LogProtocol logProtocol = new LogProtocol();
        logProtocol.setMethod(request.getMethod());
        logProtocol.setProtocol(request.getProtocol());
        logProtocol.setRemoteAddr(request.getRemoteAddr());
        logProtocol.setRequestURI(request.getRequestURI());
        logProtocol.setRequestURL(request.getServerName());
        logProtocol.setServerPort(String.valueOf(request.getServerPort()));
        logProtocol.setRemoteUser(request.getRemoteUser());
        logProtocol.setServletPath(request.getServletPath());
        logProtocol.setRemotePort(String.valueOf(request.getRemotePort()));
        logProtocol.setLocalPort(String.valueOf(request.getLocalPort()));
        logProtocol.setLocalAddr(request.getLocalAddr());
        logProtocol.setAddedTime(LocalDateTime.now(ZoneId.of("Asia/Tashkent")));
        logProtocol.setCreatedDate(LocalDate.now(ZoneId.of("Asia/Tashkent")));
        Long logId = saveToDB(logProtocol);

        //getClientIpAddr((HttpServletRequest) servlet);

        requestService.createLog(RequestProtocol.builder()
                .logProtocolId(logId)
                .param(body)
                .subSystem(subSystem.toString())
                .build());
    }


    public static void getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info("Proxy-Client-IP : {}", ip);
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info("WL-Proxy-Client-IP : {}", ip);
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info("HTTP_X_FORWARDED_FOR : {}", ip);
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED");
            log.info("HTTP_X_FORWARDED : {}", ip);
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
            log.info("HTTP_X_CLUSTER_CLIENT_IP : {}", ip);
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info("HTTP_CLIENT_IP : {}", ip);
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
            log.info("HTTP_FORWARDED_FOR : {}", ip);
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_FORWARDED");
            log.info("HTTP_FORWARDED : {}", ip);
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_VIA");
            log.info("HTTP_VIA : {}", ip);
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("REMOTE_ADDR");
            log.info("REMOTE_ADDR : {}", ip);
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
            log.info("Last chance IP : {}", ip);
        }
    }

    @Transactional
    public Long saveToDB(LogProtocol logProtocol) {
        log.info("\nSaving log to Database:" +
                        "\nmethod: ................. {}" +
                        "\nremoteAddr: ............. {}" +
                        "\nrequestURI: ............. {}" +
                        "\naddedTime: .............. {}",
                logProtocol.getMethod(), logProtocol.getRemoteAddr(), logProtocol.getRequestURI(), logProtocol.getAddedTime());
        return repository.save(logProtocol).getId();
    }
}
