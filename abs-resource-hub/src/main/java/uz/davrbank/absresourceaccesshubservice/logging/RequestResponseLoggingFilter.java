package uz.davrbank.absresourceaccesshubservice.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final int MAX_PAYLOAD_LENGTH = 10_000;
    private static final int REQUEST_CACHE_LIMIT = 10_240;
    private static final List<MediaType> VISIBLE_TYPES = List.of(
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_HTML,
            MediaType.TEXT_XML
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = wrapRequest(request);
        ContentCachingResponseWrapper wrappedResponse = wrapResponse(response);

        String exchangeId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            if (!isAsyncStarted(wrappedRequest)) {
                logIncomingRequest(exchangeId, wrappedRequest);
                logOutgoingResponse(exchangeId, wrappedRequest, wrappedResponse, System.currentTimeMillis() - startTime);
            }
            wrappedResponse.copyBodyToResponse();
        }
    }

    private ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper wrapper) {
            return wrapper;
        }
        return new ContentCachingRequestWrapper(request, REQUEST_CACHE_LIMIT);
    }

    private ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper wrapper) {
            return wrapper;
        }
        return new ContentCachingResponseWrapper(response);
    }

    private void logIncomingRequest(String exchangeId, ContentCachingRequestWrapper request) {
        log.info(
                "Incoming request [{}] method={} uri={} query={} clientIp={} contentType={} body={}",
                exchangeId,
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(),
                request.getRemoteAddr(),
                request.getContentType(),
                getPayload(request.getContentAsByteArray(), request.getCharacterEncoding(), request.getContentType())
        );
    }

    private void logOutgoingResponse(
            String exchangeId,
            ContentCachingRequestWrapper request,
            ContentCachingResponseWrapper response,
            long durationMs
    ) {
        log.info(
                "Outgoing response [{}] method={} uri={} status={} durationMs={} contentType={} body={}",
                exchangeId,
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                durationMs,
                response.getContentType(),
                getPayload(response.getContentAsByteArray(), response.getCharacterEncoding(), response.getContentType())
        );
    }

    private String getPayload(byte[] content, String encoding, String contentType) {
        if (content == null || content.length == 0) {
            return "<empty>";
        }

        if (!isVisibleContentType(contentType)) {
            return "<non-textual content omitted>";
        }

        Charset charset = StandardCharsets.UTF_8;
        if (encoding != null && Charset.isSupported(encoding)) {
            charset = Charset.forName(encoding);
        }

        String payload = new String(content, charset);
        if (payload.length() > MAX_PAYLOAD_LENGTH) {
            return payload.substring(0, MAX_PAYLOAD_LENGTH) + "...(truncated)";
        }

        return payload;
    }

    private boolean isVisibleContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return true;
        }

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(contentType);
        } catch (IllegalArgumentException exception) {
            return false;
        }

        if ("text".equalsIgnoreCase(mediaType.getType())) {
            return true;
        }

        String subtype = mediaType.getSubtype().toLowerCase(Locale.ROOT);
        if (subtype.contains("json") || subtype.contains("xml")) {
            return true;
        }

        return VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
    }
}
