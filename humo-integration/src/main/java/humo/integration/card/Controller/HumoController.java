package humo.integration.card.Controller;

import humo.integration.card.Model.CheckCardToPinflRequestDTO;
import humo.integration.card.Model.Dto.*;
import humo.integration.card.Model.Request.QueryTransHistoryReq;
import humo.integration.card.Service.CardActionService;
import humo.integration.card.Service.FeignService;
import humo.integration.card.Service.LogProtocolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class HumoController {

    @Autowired
    FeignService feignService;
    @Autowired
    CardActionService cardActionService;
    @Autowired
    LogProtocolService logProtocolService;


    @PostMapping("/cardsByPhone")
    public ResponseEntity<?> findCustomerCardsByPhone(@Valid @RequestBody HumoPhoneDto humoPhoneDto, ServletRequest servlet) {
        //  cardActionService.createNewAction(humoPhoneDto, "cardsByPhone", logProtocolService.addLog(servlet).getId());
        // logProtocolService.addLog(servlet);
        return new ResponseEntity<>(feignService.findCustomerCardsByPhone(humoPhoneDto), HttpStatus.OK);
    }

    @PostMapping("/cards")
    public ResponseEntity<?> findCustomerCardInfo(@Valid @RequestBody HumoCardDto humoCardDto, ServletRequest servlet) throws Exception {
        return new ResponseEntity<>(feignService.findCustomerInfo(humoCardDto), HttpStatus.OK);
    }

    @PostMapping("/p2pInfo")
    public ResponseEntity<?> findCustomerP2PInfo(@Valid @RequestBody HumoCardDto humoCardDto) {
        return new ResponseEntity<>(feignService.findCustomerP2PInfo(humoCardDto), HttpStatus.OK);
    }


    @PostMapping("/tranHistory")
    public ResponseEntity<?> findCardHistory(@Valid @RequestBody QueryTransHistoryReq req, ServletRequest servlet) {
        req.setBeginDate(req.getBeginDate() + "T00:00:00");
        req.setFinishDate(req.getFinishDate() + "T23:59:59");
        //  cardActionService.createNewAction(req, "tranHistory", logProtocolService.addLog(servlet).getId());
        // logProtocolService.addLog(servlet);
        return new ResponseEntity<>(feignService.findCardHistory(req), HttpStatus.OK);
    }

//    @GetMapping("/maskedPan")
//    public ResponseEntity<?> findCardMaskedPan(@Valid @RequestParam("cardNumber") String cardNumber, ServletRequest servlet) {
//        return new ResponseEntity<>(feignService.findCardToken(cardNumber), HttpStatus.OK);
//    }

    @GetMapping("/maskedPan")
    public ResponseEntity<?> findCardMaskedPan(@Valid @RequestParam("cardNumber") String cardNumber, ServletRequest servlet) {
        // cardActionService.createNewAction(cardNumber, "cardNumber", logProtocolService.addLog(servlet).getId());
        // logProtocolService.addLog(servlet);
        return new ResponseEntity<>(feignService.findCardToken2(cardNumber), HttpStatus.OK);
    }


    @PostMapping("/smsOn")
    public ResponseEntity<?> cardSmsActivate(@RequestBody Object request, HttpServletRequest servlet) {
        cardActionService.createNewAction(request, "smsOn", logProtocolService.addLog(servlet).getId());
        return new ResponseEntity<>(feignService.cardActivateSms(request), HttpStatus.OK);
    }

    @PostMapping("/smsOff")
    public ResponseEntity<?> customerSmsDeactivate(@RequestBody Object request) {
        // cardActionService.createNewAction(cardNumber, "cardNumber", logProtocolService.addLog(servlet).getId());
        // logProtocolService.addLog(servlet);
        return new ResponseEntity<>(feignService.customerDeactivateSms(request), HttpStatus.OK);
    }


//    @GetMapping("/realPan")
//    public ResponseEntity<?> findCardRealPan(@Valid @RequestParam("maskedPan") String maskedPan, ServletRequest servlet) {
//
//        return new ResponseEntity<>(feignService.findCardRealPan(maskedPan), HttpStatus.OK);
//    }

    @GetMapping("/realPan")
    public ResponseEntity<?> findCardRealPan(@Valid @RequestParam("maskedPan") String maskedPan, ServletRequest servlet) {
        // cardActionService.createNewAction(maskedPan, "maskedPan", logProtocolService.addLog(servlet).getId());
        //  logProtocolService.addLog(servlet);
        return new ResponseEntity<>(feignService.findCardRealPan2(maskedPan), HttpStatus.OK);
    }

//    @GetMapping("/realPan")
//    public Flux<Object> findCardRealPan(@Valid @RequestParam("maskedPan") String maskedPan, ServletRequest servlet) {
//       return  Flux.create((FluxSink<Object> sink) -> {
//            sink.next(feignService.humoRealPan2(maskedPan));
//            sink.next(feignService.findCardRealPan(maskedPan));
//        }).take(1);
//    }

    @PostMapping("/createVirtual")
    public ResponseEntity<?> createNewVirtualCard(@RequestBody @Valid HumoVirtualDto dto) {
        return ResponseEntity.ok(feignService.createVirtualCard(dto));
    }

    @PostMapping("/sms/activate")
    public ResponseEntity<?> smsInfoOn(@RequestBody Object request, ServletRequest servlet) {
        cardActionService.createNewAction(request, "smsOn", logProtocolService.addLog(servlet).getId());
        return ResponseEntity.ok(feignService.onSmsInfo(request));
    }

    @PostMapping("/clientCards")
    public ResponseEntity<?> getClientHumoVirtualCards(@RequestBody @Valid HumoVirtualCardsDto dto) {
        return ResponseEntity.ok(feignService.getClientCards(dto));
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> blockOrUnBlockCard(@RequestBody @Valid CardBlockDto dto) {
        return ResponseEntity.ok(feignService.blockOrUnBlockCard(dto));
    }

    @GetMapping("/bins")
    public ResponseEntity<?> getBinList() {
        return ResponseEntity.ok(feignService.getBinList());
    }

    @PostMapping("/checkCardToPersonCode")
    public ResponseEntity<?> checkCardToPersonCode(@RequestBody @Valid CheckCardToPinflRequestDTO requestDTO) {
        return ResponseEntity.ok(feignService.checkCard2PINFL(requestDTO));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put("error", "400");
            errors.put("description", "Required fields not exist");
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
