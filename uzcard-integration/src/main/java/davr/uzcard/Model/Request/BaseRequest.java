package davr.uzcard.Model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseRequest<P> {
    @JsonProperty("id")
    public String id;
    @JsonProperty("jsonrpc")
    public String jsonRPC;
    @JsonProperty("method")
    public String method;
    @JsonProperty("params")
    public P params;

    public BaseRequest(String method, P params) {
        this.id = "dv_int_" + System.currentTimeMillis();
        this.jsonRPC = "2.0";
        this.method = method;
        this.params = params;
    }
}
