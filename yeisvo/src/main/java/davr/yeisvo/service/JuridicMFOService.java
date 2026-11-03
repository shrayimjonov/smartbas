package davr.yeisvo.service;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import davr.yeisvo.dto.request.JuridicClientRequest;
import davr.yeisvo.dto.response.GetMfoJuridicResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@JsonRpcService("api/v1/list-inn")
public interface JuridicMFOService {

    @JsonRpcMethod("get.inn.list")
    GetMfoJuridicResponse getJuridicMFO(@JsonRpcParam("request") JuridicClientRequest request);


}