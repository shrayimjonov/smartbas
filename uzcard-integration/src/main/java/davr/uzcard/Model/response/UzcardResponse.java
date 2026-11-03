package davr.uzcard.Model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UzcardResponse<R> {
    private String id;
    private String jsonrpc;
    private R result;
    private Error error;

    public UzcardResponse(long l, String s, Error error) {
        this.id = String.valueOf(l);
        this.jsonrpc = s;
        this.error = error;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Error {
        public Long code;
        public String message;
    }
}
