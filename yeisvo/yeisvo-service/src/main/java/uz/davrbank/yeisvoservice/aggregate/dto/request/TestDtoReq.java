package uz.davrbank.yeisvoservice.aggregate.dto.request;


import lombok.Data;

import java.util.UUID;

@Data
public class TestDtoReq {
    private String url;
    private String operationId = UUID.randomUUID().toString();

}
