package davr.yeisvo.dto.response;

import lombok.Data;

@Data
public class BaseResponse {
    private int status;
    private String errorMsg;
    private String timeStamp;
}
