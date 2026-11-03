package uz.davrbank.remittanceoutside.aggregate.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uz.davrbank.remittanceoutside.aggregate.configuration.ApplicationProperties;
import uz.davrbank.remittanceoutside.aggregate.dto.ApiExchangeLog;
import uz.davrbank.remittanceoutside.aggregate.enums.ExchangeType;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiExchangeLogger {

    private final ObjectMapper objectMapper;
    private final ApplicationProperties applicationProperties;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.topic.name}")
    private String topicName;

    private static final int MAX_PAYLOAD = 5000;


    public void logInboundRequest(String eventId,
                                  String method,
                                  String url,
                                  String clientIp,
                                  Map<String, List<String>> headers,
                                  String body) {
        ApiExchangeLog logModel = base(eventId, method, url, clientIp);
        logModel.setEventType(ExchangeType.INBOUND);
        logModel.setExchangeType("request");
        logModel.setEventHeader(serializeHeaders(headers));
        logModel.setEventJson(maskAndTruncate(body));
        info("rest.inbound.request", logModel);
    }

    public void logInboundResponse(String eventId,
                                   String method,
                                   String url,
                                   String clientIp,
                                   Map<String, List<String>> headers,
                                   String body,
                                   String status,
                                   Long durationMs) {
        ApiExchangeLog logModel = base(eventId, method, url, clientIp);
        logModel.setEventType(ExchangeType.OUTBOUND);
        logModel.setExchangeType("response");
        logModel.setEventHeader(serializeHeaders(headers));
        logModel.setEventJson(maskAndTruncate(body));
        logModel.setResponseStatus(status);
        logModel.setResponseTime(durationMs);
        info("rest.inbound.response", logModel);
    }

    public void logThirdPartyRequest(String eventId,
                                     String method,
                                     String url,
                                     Map<String, List<String>> headers,
                                     String body) {
        ApiExchangeLog logModel = base(eventId, method, url, null);
        logModel.setEventType(ExchangeType.THIRD_PARTY_REQUEST);
        logModel.setExchangeType("request");
        logModel.setEventHeader(serializeHeaders(headers));
        logModel.setEventJson(maskAndTruncate(body));
        info("integration.third.party.request", logModel);
    }

    public void logThirdPartyResponse(String eventId,
                                      String method,
                                      String url,
                                      Map<String, List<String>> headers,
                                      String body,
                                      String status,
                                      Long durationMs) {
        ApiExchangeLog logModel = base(eventId, method, url, null);
        logModel.setEventType(ExchangeType.THIRD_PARTY_RESPONSE);
        logModel.setExchangeType("response");
        logModel.setEventHeader(serializeHeaders(headers));
        logModel.setEventJson(maskAndTruncate(body));
        logModel.setResponseStatus(status);
        logModel.setResponseTime(durationMs);
        info("integration.third.party.response", logModel);
    }

    public void logThirdPartyError(String eventId,
                                   String method,
                                   String url,
                                   String additionals,
                                   Long durationMs) {
        ApiExchangeLog logModel = base(eventId, method, url, null);
        logModel.setEventType(ExchangeType.THIRD_PARTY_RESPONSE);
        logModel.setExchangeType("response");
        logModel.setAdditionals(additionals);
        logModel.setResponseTime(durationMs);
        error(logModel);
    }

    private ApiExchangeLog base(String eventId, String method, String url, String clientIp) {
        ApiExchangeLog m = new ApiExchangeLog();
        m.setEventId(eventId);
        m.setEventDate(OffsetDateTime.now());
        m.setHttpMethod(method);
        m.setUrl(url);
        m.setClientIp(clientIp);
        return m;
    }

    private String maskAndTruncate(String body) {
        if (!StringUtils.hasText(body)) return body;
        String masked = maskSensitive(body);
        return maybeTruncate(masked);
    }

    private String serializeHeaders(Map<String, List<String>> headers) {
        if (headers == null) return null;
        try {
            return objectMapper.writeValueAsString(headers);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private String toJson(ApiExchangeLog logModel) {
        try {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("event_id", logModel.getEventId());
            m.put("event_type", logModel.getEventType() != null ? logModel.getEventType().name() : null);
            m.put("client_ip", logModel.getClientIp());
            String dt = null;
            if (logModel.getEventDate() != null) {
                dt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(java.time.ZoneOffset.UTC)
                        .format(logModel.getEventDate().toInstant());
            }
            m.put("event_date", dt);
            m.put("url", logModel.getUrl());
            m.put("http_method", logModel.getHttpMethod());
            m.put("event_header", logModel.getEventHeader());
            m.put("event_json", logModel.getEventJson());
            m.put("additionals", logModel.getAdditionals());
            m.put("response_status", logModel.getResponseStatus());
            m.put("response_time", logModel.getResponseTime());
            m.put("exchange_type", logModel.getExchangeType());
            return objectMapper.writeValueAsString(m);
        } catch (Exception e) {
            log.error("serialize.log.failed eventId={} err={}", logModel.getEventId(), e.toString());
            throw new RuntimeException("Failed to serialize ApiExchangeLog", e);
        }
    }

    private void info(String prefix, ApiExchangeLog logModel) {
        String json = toJson(logModel);
        try {
            log.info("{}: {}", prefix, json);
        } catch (Exception ex) {
            log.info("{} method={} url={} fallback", prefix, logModel.getHttpMethod(), logModel.getUrl());
        }
        try {
            kafkaTemplate.send(topicName, json)
                    .whenComplete((res, ex) -> {
                        if (ex != null) {
                            log.error("kafka.send.failed key={} err={}",
                                    logModel.getEventId(), ex.toString());
                        }
                    });
        } catch (Exception e) {
            log.error("kafka.send.exception key={} err={}",
                    logModel.getEventId(), e.toString());
        }
    }

    private void error(ApiExchangeLog logModel) {
        String json = toJson(logModel);
        log.error("{}: {}", "integration.third.party.error", json);
        try {
            kafkaTemplate.sendDefault(logModel.getEventId(), json)
                    .whenComplete((res, ex) -> {
                        if (ex != null) {
                            log.error("kafka.send.failed key={} err={}",
                                    logModel.getEventId(), ex.toString());
                        }
                    });
        } catch (Exception e) {
            log.error("kafka.send.exception key={} err={}",
                    logModel.getEventId(), e.toString());
        }
    }

    private String maskSensitive(String body) {
        try {
            if (isCorrectJsonStructure(body)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = objectMapper.readValue(body, Map.class);
                Set<String> keysToMask = new HashSet<>();
                List<String> cfg = applicationProperties.getLoggingSensitiveData() != null
                        ? applicationProperties.getLoggingSensitiveData().getSensitiveBodyKeys()
                        : null;
                if (cfg != null) {
                    for (String k : cfg) {
                        if (k != null) keysToMask.add(k.toLowerCase(Locale.ROOT).trim());
                    }
                }
                if (!keysToMask.isEmpty()) {
                    maskKeys(map, keysToMask);
                    return objectMapper.writeValueAsString(map);
                }
            }
        } catch (Exception ignored) {
        }
        return body;
    }

    @SuppressWarnings("unchecked")
    private void maskKeys(Map<String, Object> map, Set<String> keysToMask) {
        for (Map.Entry<String, Object> e : map.entrySet()) {
            String key = e.getKey();
            Object val = e.getValue();
            if (val instanceof Map) {
                maskKeys((Map<String, Object>) val, keysToMask);
            } else if (val instanceof List) {
                for (Object item : (List<?>) val) {
                    if (item instanceof Map) maskKeys((Map<String, Object>) item, keysToMask);
                }
            } else if (val instanceof String) {
                if (keysToMask.contains(key.toLowerCase(Locale.ROOT))) {
                    e.setValue("******");
                }
            }
        }
    }

    private boolean isCorrectJsonStructure(String s) {
        String t = s.trim();
        return (t.startsWith("{") && t.endsWith("}")) || (t.startsWith("[") && t.endsWith("]"));
    }

    private String maybeTruncate(String s) {
        if (s == null) return null;
        return s.length() > MAX_PAYLOAD ? s.substring(0, MAX_PAYLOAD) + "...<truncated>" : s;
    }
}