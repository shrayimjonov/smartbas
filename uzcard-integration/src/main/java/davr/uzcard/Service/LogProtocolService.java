package davr.uzcard.Service;


import davr.uzcard.Entity.LogProtocol;
import davr.uzcard.Repository.LogProtocolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogProtocolService {
    private final LogProtocolRepository logProtocolRepository;


    public void createLog(LogProtocol logProtocol) {
        logProtocolRepository.save(logProtocol);
    }

    @Async
    public void addLog(Object value, ServletRequest servlet) {
        LogProtocol logProtocol = new LogProtocol();
        HttpServletRequest request = (HttpServletRequest) servlet;
        logProtocol.setMethod(request.getMethod());
        logProtocol.setProtocol(request.getProtocol());
        logProtocol.setRemoteAddr(request.getRemoteAddr());
        logProtocol.setRequestURI(request.getRequestURI());
        logProtocol.setRequestURL(request.getServerName());
        logProtocol.setLocalDateTime(LocalDateTime.now());
        logProtocol.setServerPort(String.valueOf(request.getServerPort()));
        logProtocol.setServletPath(request.getServletPath());
        logProtocol.setRemotePort(String.valueOf(request.getRemotePort()));
        logProtocol.setLocalPort(String.valueOf(request.getLocalPort()));
        logProtocol.setLocalAddr(request.getLocalAddr());
        logProtocol.setServiceName("UZCARD_INTEGRATION");
        logProtocol.setRequestedSessionId(value.toString());
        createLog(logProtocol);
    }

}
