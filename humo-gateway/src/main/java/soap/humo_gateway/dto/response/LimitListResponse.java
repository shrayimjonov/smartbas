package soap.humo_gateway.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LimitListResponse {
    @JacksonXmlProperty(localName = "Body")
    public LimitResponseBody Body;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LimitResponseBody {

        @JacksonXmlProperty(localName = "getLimitsResponse")
        private GetLimitsResponse getLimitsResponse;
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GetLimitsResponse {
        @JacksonXmlProperty(localName = "limit_event")
        private LimitEvent limit_event;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LimitEvent {

        @JacksonXmlProperty(localName = "key_type")
        private String key_type;

        @JacksonXmlProperty(localName = "key_value")
        private String key_value;

        @JacksonXmlProperty(localName = "code_owner")
        private String code_owner;

        @JacksonXmlProperty(localName = "limit")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Limit> limit;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Limit {

        @JacksonXmlProperty(localName = "subject_value")
        private String subject_value;

        @JacksonXmlProperty(localName = "limit_type")
        private String limit_type;

        @JacksonXmlProperty(localName = "limit_value")
        private String limit_value;

        @JacksonXmlProperty(localName = "limit_name")
        private String limit_name;

        @JacksonXmlProperty(localName = "limit_date_from")
        private String limit_date_from;

        @JacksonXmlProperty(localName = "limit_date_to")
        private String limit_date_to;
    }

}
