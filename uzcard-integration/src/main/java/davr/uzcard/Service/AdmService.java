package davr.uzcard.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import davr.uzcard.Model.response.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdmService {
    private final FeignImpl feignService;
    private final LogProtocolService logProtocolService;
    private final ObjectMapper objectMapper;

    public <E> Object svgateConnect2(E value, HttpServletRequest request) {
        logProtocolService.addLog(value, request);
        log.info("svgateConnect2 Request: {}", value);
        try {
            Object response;
            if (isHistoryMethod(value)) {
                log.info("svgateHistory request Method");
                response =  feignService.svgateHistory(value);
            } else {
                response = feignService.svgateConnect(value);
            }
            log.info("svgateConnect2 response : {}", response);
            return response;
        } catch (Exception ex) {
            log.error("response error: ", ex);
            return new ResponseData<>(false, 400L, ex.getMessage());
        }
    }

    public <E> Object boGateConnect(E value, HttpServletRequest request) {
        logProtocolService.addLog(value, request);
        log.info("boGateConnect Request: {}", value);
        try {
            var response = feignService.boGateConnect(value);
            log.info("boGateConnect response : {}", response);
            return response;
        } catch (Exception ex) {
            log.error("response error: ", ex);
            return new ResponseData<>(false, 400L, ex.getMessage());
        }
    }

    private <E> boolean isHistoryMethod(E value) {
        log.info("isHistoryMethod Request: {}", value);
        try {
            JsonNode node = objectMapper.valueToTree(value);
            String method = node.path("method").asText("");
            return method.contains("history");
        } catch (Exception e) {
            log.warn("Could not parse method from request: {}", e.getMessage());
            return false;
        }
    }

}
