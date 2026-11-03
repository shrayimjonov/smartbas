package nci.billing.controller;

import jakarta.validation.Valid;
import nci.billing.model.dto.ForexTranRequestDTO;
import nci.billing.model.dto.SimpleTranRequestDTO;
import nci.billing.service.CoreService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/abs")
public class ABSController {
    private final CoreService coreService;

    public ABSController(@Qualifier(value = "coreImpl") CoreService coreService) {
        this.coreService = coreService;
    }


    @PostMapping("/forex")
    public ResponseEntity<?> doForex(@RequestBody @Valid ForexTranRequestDTO requestDTO) {
        return ResponseEntity.ok(
                coreService.forexOperation(requestDTO)
        );
    }

    @PostMapping("/doTran")
    public ResponseEntity<?> doSimpleTransaction(@RequestBody @Valid SimpleTranRequestDTO requestDTO) {
        return ResponseEntity.ok(
                coreService.simpleTransaction(requestDTO)
        );
    }

}
