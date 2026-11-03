package davr.uzcard.Controller;


import davr.uzcard.Service.AdmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/adm/v1")
@RequiredArgsConstructor
public class ADMController {
    private final AdmService admService;

    @PostMapping("/svGate")
    public ResponseEntity<?> connect(@RequestBody Object o, HttpServletRequest request) {
        return ResponseEntity.ok(admService.svgateConnect2(o, request));
    }

    @PostMapping("/boGate")
    public ResponseEntity<?> boGateConnect(@RequestBody Object o, HttpServletRequest request) {
        return ResponseEntity.ok(admService.boGateConnect(o, request));
    }

}
