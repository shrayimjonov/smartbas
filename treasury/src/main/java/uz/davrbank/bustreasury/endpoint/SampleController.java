package uz.davrbank.bustreasury.endpoint;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@OpenAPIDefinition(info = @Info(title = "My API", version = "1.0", description = "Sample API for bus-treasury"))
@RestController
public class SampleController {

    @GetMapping("/hello")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved message"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String hello() {
        return "Hello, World!";
    }
}