package soap.humo_gateway.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import soap.humo_gateway.dto.response.core.FaultDto;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class LimitResponse {
    @JacksonXmlProperty(localName = "Body")
    private Body body;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static class Body {
        @JacksonXmlProperty(localName = "setLimitsResponse")
        private SetLimitsResponse setLimitsResponse;
        @JacksonXmlProperty(localName = "Fault")
        private FaultDto fault;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static class SetLimitsResponse {
        @JacksonXmlProperty(localName = "limit_event")
        private LimitEvent limitEvent;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static class LimitEvent{
        @JacksonXmlProperty(localName = "key_type")
        private String keyType;
        @JacksonXmlProperty(localName = "key_value")
        private String keyValue;
        @JacksonXmlProperty(localName = "limit")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Limit> limit;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static class Limit {
        @JacksonXmlProperty(localName = "subject_value")
        private String subjectValue;
        @JacksonXmlProperty(localName = "limit_type")
        private String limitType;
        @JacksonXmlProperty(localName = "limit_value")
        private Long limitValue;
        @JacksonXmlProperty(localName = "limit_name")
        private String limitName;
        @JacksonXmlProperty(localName = "limit_date_from")
        private String limitDateFrom;
        @JacksonXmlProperty(localName = "limit_date_to")
        private String limitDateTo;
    }
}
