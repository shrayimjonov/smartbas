package davr.yeisvo.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class TestDtoReq {
    private String url;
    private String operationId = UUID.randomUUID().toString();

}
