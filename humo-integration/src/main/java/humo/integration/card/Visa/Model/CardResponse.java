package humo.integration.card.Visa.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import humo.integration.card.Model.Dto.core.response.CoreError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardResponse {

    @JsonProperty("jsonrpc")
    private String jsonrpc;
    @JsonProperty("id")
    private String id;

    @JsonProperty("result")
    private List<Result> result;

    @JsonProperty("error")
    private CoreError error;

    @JsonProperty("jsonrpc")
    public String getJsonrpc() {
        return jsonrpc;
    }

    @JsonProperty("jsonrpc")
    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("result")
    public Object getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(List<Result> result) {
        this.result = result;
    }


    @Data
    @Builder
    public static class Result{
        public String id;
        public String username;
        public String pan;
        public String expiry;
        public int status;
        public String phone;
        public String fullName;
        public Long balance;
        public boolean sms;
        public int pincnt;
        public String cardtype;
        public Long holdAmount;
        public Long cashbackAmount;
        public String aacct;
        public String par;
    }


}
