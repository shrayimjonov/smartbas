package nci.billing.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseIsAPIResponse {
    @JsonProperty("result")
    public String result;
    @JsonProperty("resultnote")
    public String resultNote;
    @JsonProperty("docid")
    public String docId;
    @JsonProperty("id")
    public String id;
}
