package davr.uzcard.Model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Getter
@Setter
@ToString
public class ResponseData<Data> extends Result implements Serializable {
    private Data data;
    private Instant timeStamp = Instant.now(Clock.system(ZoneId.of("Asia/Tashkent")));

    public ResponseData(Data data) {
        super(true);
        this.data = data;
    }


    public ResponseData(boolean success) {
        super(success);
    }

    public ResponseData(boolean success, Long code, String message) {
        super(success, code, message);
    }
}
