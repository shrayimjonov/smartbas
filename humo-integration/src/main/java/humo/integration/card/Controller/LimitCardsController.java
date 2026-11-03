package humo.integration.card.Controller;


import humo.integration.card.Service.LimitCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Karta limitlarini boshqarish uchun mo'ljallangan REST controller.
 * Limit balansini olish, limitlarni o'rnatish, tahrirlash va o'chirish
 */
@RestController
@RequestMapping("/api/v1/limit")
@RequiredArgsConstructor
public class LimitCardsController {

    private final LimitCardService limitCardService;


    @PostMapping("/balance")
    public ResponseEntity<?> getLimitBalance(@RequestBody Object request) {
        return ResponseEntity.ok(limitCardService.getLimitBalance(request));
    }


    @PostMapping("/ref-data")
    public ResponseEntity<?> getRefData() {
        return ResponseEntity.ok(limitCardService.getRefData());
    }


    @PostMapping("/set")
    public ResponseEntity<?> setAnyLimit(@RequestBody Object request) {
        return ResponseEntity.ok(limitCardService.setAnyLimit(request));
    }


    @PutMapping("/edit")
    public ResponseEntity<?> editAnyLimit(@RequestBody Object request) {
        return ResponseEntity.ok(limitCardService.editAnyLimit(request));
    }


    @PostMapping("/subject-value")
    public ResponseEntity<?> getSubjectValue(@RequestBody Object request) {
        return ResponseEntity.ok(limitCardService.getSubjectValue(request));
    }


    @PostMapping("/delete")
    public ResponseEntity<?> deleteLimit(@RequestBody Object request) {
        return ResponseEntity.ok(limitCardService.deleteLimit(request));
    }


    @PostMapping("/delete-all")
    public ResponseEntity<?> deleteAllLimit(@RequestBody Object request) {
        return ResponseEntity.ok(limitCardService.deleteAllLimit(request));
    }
}
