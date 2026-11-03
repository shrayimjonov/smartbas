package uz.davrbank.remittanceoutside.aggregate.aop;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 17.11.2025
 * Time: 11:30
 * Project: remittance-outside
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class RestControllerLoggingFilter extends OncePerRequestFilter {

    private final ApiExchangeLogger apiExchangeLogger;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator/health") || path.startsWith("/actuator/prometheus")
                || path.startsWith("/swagger") || path.startsWith("/v3/api-docs")
                || path.startsWith("/webjars/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.nanoTime();

        String eventId = UUID.randomUUID().toString();
        MDC.put("eventId", eventId);

        ContentCachingRequestWrapper reqWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper respWrapper = new ContentCachingResponseWrapper(response);

        // Log request
        try {
            String method = reqWrapper.getMethod();
            String url = reqWrapper.getRequestURL().toString() + (reqWrapper.getQueryString() != null ? ("?" + reqWrapper.getQueryString()) : "");
            String clientIp = resolveClientIp(reqWrapper);
            Map<String, List<String>> headers = extractHeaders(reqWrapper);
            String body = readRequestBody(reqWrapper);
            apiExchangeLogger.logInboundRequest(eventId, method, url, clientIp, headers, body);
        } catch (Exception e) {
            log.error("Failed to log request: {}", e.getMessage());
        }

        try {
            filterChain.doFilter(reqWrapper, respWrapper);
        } finally {
            long durMs = (System.nanoTime() - start) / 1_000_000;
            try {
                String method = reqWrapper.getMethod();
                String url = reqWrapper.getRequestURL().toString() + (reqWrapper.getQueryString() != null ? ("?" + reqWrapper.getQueryString()) : "");
                String clientIp = resolveClientIp(reqWrapper);
                Map<String, List<String>> headers = extractHeaders(respWrapper);
                String body = readResponseBody(respWrapper);
                apiExchangeLogger.logInboundResponse(eventId, method, url, clientIp, headers, body, String.valueOf(respWrapper.getStatus()), durMs);
            } catch (Exception e) {
                log.error("Failed to log response: {}", e.getMessage());
            } finally {
                MDC.remove("eventId");
            }
            respWrapper.copyBodyToResponse();
        }
    }

    private String resolveClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xf)) {
            return xf.split(",")[0].trim();
        }
        String rp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(rp)) return rp;
        return request.getRemoteAddr();
    }

    private String readRequestBody(ContentCachingRequestWrapper request) {
        byte[] buf = request.getContentAsByteArray();
        if (buf.length == 0) return null;
        Charset cs = resolveCharset(request.getCharacterEncoding());
        return new String(buf, cs);
    }

    private String readResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        if (buf.length == 0) return null;
        String charset = Optional.ofNullable(response.getCharacterEncoding()).orElse("UTF-8");
        return new String(buf, Charset.forName(charset));
    }

    private Charset resolveCharset(String enc) {
        try {
            return enc != null ? Charset.forName(enc) : StandardCharsets.UTF_8;
        } catch (Exception e) {
            return StandardCharsets.UTF_8;
        }
    }

    private Map<String, List<String>> extractHeaders(HttpServletRequest request) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            List<String> values = Collections.list(request.getHeaders(name));
            map.put(name, values);
        }
        return map;
    }

    private Map<String, List<String>> extractHeaders(HttpServletResponse response) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (String name : response.getHeaderNames()) {
            List<String> values = new ArrayList<>(response.getHeaders(name));
            map.put(name, values);
        }
        return map;
    }
}