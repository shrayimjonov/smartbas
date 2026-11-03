//package nci.billing.config;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.util.stream.Collectors;
//
///**
// */
//@Component
//@Slf4j
//public class IpFilter implements Filter {
//
//    @Value("${access.ip}")
//    private String accessIps;
//
//    @SneakyThrows
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
//        if (request.getRemoteAddr().matches(accessIps)){
//            filterChain.doFilter(request, response);
//        }
//        else {
//            log.error("Failed to pass IpFilter: {}", request.getRemoteAddr());
//            HttpServletResponse httpResponse = (HttpServletResponse) response;
//            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            httpResponse.flushBuffer();
//        }
//
//    }
//}
