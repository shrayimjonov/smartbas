package uz.davrbank.remittanceoutside.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import uz.davrbank.remittanceoutside.aggregate.enums.ExchangeType;

import java.time.OffsetDateTime;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 17.11.2025
 * Time: 11:30
 * Project: remittance-outside
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiExchangeLog {

    @JsonProperty("event_id")
    private String eventId;               // unique id

    @JsonProperty("event_type")
    private ExchangeType eventType;     // INBOUND/OUTBOUND/THIRD_PARTY_RESPONSE/THIRD_PARTY_REQUEST

    // Request basics
    @JsonProperty("client_ip")
    private String clientIp;

    @JsonProperty("event_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime eventDate;

    @JsonProperty("url")
    private String url;

    @JsonProperty("http_method")
    private String httpMethod;

    // Payload
    @JsonProperty("event_header")
    private String eventHeader;

    @JsonProperty("event_json")
    private String eventJson;

    @JsonProperty("additionals")
    private String additionals;          // any extra info

    // Response
    @JsonProperty("response_status")
    private String responseStatus;

    @JsonProperty("response_time")
    private Long responseTime;          // in millis

    @JsonProperty("exchange_type")
    private String exchangeType;        // "request" or "response"
}