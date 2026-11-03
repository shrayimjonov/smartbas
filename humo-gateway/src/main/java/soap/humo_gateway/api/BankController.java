package soap.humo_gateway.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import soap.humo_gateway.service.BinsService;

@RestController
@RequestMapping("/entity")
@RequiredArgsConstructor
public class BankController {

    private final BinsService binsService;

    @GetMapping("/bins")
    public ResponseEntity<?> findAllBinsList() {
        return ResponseEntity.ok(binsService.findAllBanks());
    }

    @GetMapping("/bin")
    public ResponseEntity<?> getBinInfoByBin(@RequestParam("bin") String bin) {
        return ResponseEntity.ok(binsService.findBankCodeByBin(bin));
    }


}
