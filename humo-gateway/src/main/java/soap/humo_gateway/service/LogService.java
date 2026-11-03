package soap.humo_gateway.service;

import org.springframework.scheduling.annotation.Async;
import soap.humo_gateway.dto.enums.SubSystem;

import javax.servlet.ServletRequest;

public interface LogService {
    void createLog(ServletRequest request, SubSystem subSystem, String body);
}
