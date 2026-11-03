package davr.uzcard.Controller;

import davr.uzcard.Model.ParamDto;
import davr.uzcard.Service.ParamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/params")
@RequiredArgsConstructor
public class ParamController {
    private final ParamsService service;

    @PostMapping
    public ResponseEntity<?> addParam(@RequestBody @Valid ParamDto dto) {
        return ResponseEntity.ok(service.addParam(dto));
    }



}
