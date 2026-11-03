package humo.integration.card.Service;



import humo.integration.card.Entity.LogProtocol;
import humo.integration.card.Repository.LogProtocolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogProtocolService {
    private final LogProtocolRepository logProtocolRepository;


    public LogProtocol createLog(LogProtocol logProtocol) {
        return logProtocolRepository.save(logProtocol);
    }

    public LogProtocol addLog(ServletRequest servlet) {
        LogProtocol logProtocol = new LogProtocol();
        HttpServletRequest request = (HttpServletRequest) servlet;
        logProtocol.setMethod(request.getMethod());
        logProtocol.setProtocol(request.getProtocol());
        logProtocol.setRemoteAddr(request.getRemoteAddr());
        logProtocol.setRequestedSessionId(request.getRequestedSessionId());
        logProtocol.setRequestURI(request.getRequestURI());
        logProtocol.setRequestURL(request.getServerName());
        logProtocol.setLocalDateTime(LocalDateTime.now());
        logProtocol.setServerPort(String.valueOf(request.getServerPort()));
        logProtocol.setServletPath(request.getServletPath());
        logProtocol.setRemotePort(String.valueOf(request.getRemotePort()));
        logProtocol.setLocalPort(String.valueOf(request.getLocalPort()));
        logProtocol.setLocalAddr(request.getLocalAddr());
        logProtocol.setServiceName("HUMO_INTEGRATION");
        return createLog(logProtocol);
    }

}
