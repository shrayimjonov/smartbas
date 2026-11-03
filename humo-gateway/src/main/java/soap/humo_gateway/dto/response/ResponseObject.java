package soap.humo_gateway.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseObject {
    String response_code;
    String error_description;
    String EXTERNAL_SESSION_ID;
    String error_action;

}
