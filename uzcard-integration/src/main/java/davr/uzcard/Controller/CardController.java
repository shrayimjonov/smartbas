package davr.uzcard.Controller;

import davr.uzcard.Model.*;
import davr.uzcard.Model.Request.*;

import davr.uzcard.Service.CardActionService;
import davr.uzcard.Service.Feign.FeignService;
import davr.uzcard.Service.LogProtocolService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.*;

@RestController
@Slf4j
public class CardController {

    @Autowired
    FeignService feignService;
    @Autowired
    CardActionService cardActionService;
    @Autowired
    LogProtocolService logProtocolService;

    @Value("${uzcard.card-block.terminal_id}")
    private String terminalId;
    @Value("${uzcard.card-block.merchant_id}")
    private String merchantId;


    @PostMapping("/card")
    public ResponseEntity<?> cardsNew(@RequestBody CardsNewDto cardsNewDto, ServletRequest servletRequest) {
        log.info("/card method request: {}", cardsNewDto);

        var cardNews = CardsNew.builder()
                .id(1).jsonrpc("2.0")
                .method("cards.new")
                .params(CardsNew.Params.builder()
                        .card(CardsNew.Params
                                .Card.builder().pan(cardsNewDto.getCardPan())
                                .expiry(cardsNewDto.getExpiry())
                                .build())
                        .build())
                .build();

        Object resp = feignService.svgateConnect(cardNews);
        log.info("/card method response: {}", resp);
        //   cardActionService.createNewAction(cardNews, "cards.new", logProtocolService.addLog(servletRequest).getId());
        return new ResponseEntity<>(resp, HttpStatus.OK);

    }


    @PostMapping("/p2pinfo")
    public ResponseEntity<?> P2pInfo(@RequestBody CardsNewDto cardsNewDto, ServletRequest servletRequest) {
        log.info("/p2pinfo method request: {}", cardsNewDto);
        try {
            var p2pInfo = P2pInfo.builder()
                    .id(1).jsonrpc("2.0").method("p2p.info")
                    .params(P2pInfo.Params.builder().hpan(cardsNewDto.getCardPan()).build()).build();

            Object resp = feignService.svgateConnect(p2pInfo);
            //  cardActionService.createNewAction(p2pInfo, "p2p.info", logProtocolService.addLog(servletRequest).getId());
            log.info("/p2pinfo method response: {}", resp);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cardGet")
    public ResponseEntity<?> cardInfoByToken(@RequestBody CardTokenDto tokenDto, ServletRequest servletRequest) {
        log.info("/cardGet method request: {}", tokenDto);
        try {
            ArrayList<String> strings = new ArrayList<>();
            assert false;
            strings.add(tokenDto.getToken());
            var cardsGet = CardsGet.builder()
                    .id(1).jsonrpc("2.0").method("cards.get")
                    .params(CardsGet.Params.builder().ids(strings).build()).build();
            Object resp = feignService.svgateConnect(cardsGet);
            // cardActionService.createNewAction(cardsGet, "cards.get", logProtocolService.addLog(servletRequest).getId());
            //   cardActionService.createNewAction(p2pInfo,"p2p.info", logProtocolService.addLog(servletRequest).getId());
            log.info("/cardGet method response: {}", resp);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cardGetList")
    public ResponseEntity<?> cardInfoByTokenList(@RequestBody CardTokenListDto tokenDto, ServletRequest servletRequest) {
        log.info("/cardGetList method request: {}", tokenDto);
        try {
            var cardsGet = CardsGet.builder()
                    .id(1).jsonrpc("2.0").method("cards.get")
                    .params(CardsGet.Params.builder().ids(tokenDto.getToken()).build()).build();
            Object resp = feignService.svgateConnect(cardsGet);
            //   cardActionService.createNewAction(cardsGet, "cards.get.list", logProtocolService.addLog(servletRequest).getId());
            log.info("/cardGetList method response: {}", resp);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/cardsListToken")
    public ResponseEntity<?> getCardsByPhoneNumber(@RequestBody CardPhoneDto phoneDto, ServletRequest servletRequest) {
        log.info("/cardsListToken method request: {}", phoneDto);
        try {
            var cardsList = CardsListToken.builder()
                    .id(1).jsonrpc("2.0").method("cards.list.token")
                    .params(CardsListToken.Params.builder().phone(phoneDto.getPhone()).build()).build();
            Object resp = feignService.svgateConnect(cardsList);
            //   cardActionService.createNewAction(cardsList, "cards.list.token", logProtocolService.addLog(servletRequest).getId());
            log.info("/cardsListToken method response: {}", resp);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/cardChangeStatus")
    public ResponseEntity<?> blockOrUnBlockCard(@RequestBody CardStatusDto statusDto, ServletRequest servletRequest) {
        Random rnd = new Random();
        String stan = String.format("%06d", rnd.nextInt(999999));
        log.info("/cardChangeStatus method request: {}", statusDto);
        try {
            var cardChangeStatus = CardStatusChange.builder()
                    .id(1).jsonrpc("2.0").method("card.status.change")
                    .params(CardStatusChange.Params.builder().card(CardStatusChange.Card.builder()
                            .cardId(statusDto.getToken())
                            .ext("davr_"+System.currentTimeMillis())
                            .terminalId(terminalId).merchantId(merchantId)
                            .port("9051")
                            .stan(stan)
                            .statusId(statusDto.getStatus()).build()).build()).build();
            Object resp = feignService.svgateConnect(cardChangeStatus);
            //  cardActionService.createNewAction(cardChangeStatus, "card.status.change", logProtocolService.addLog(servletRequest).getId());
            log.info("/cardChangeStatus method response: {}", resp);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/pinResetOtp")
    public ResponseEntity<?> sendMessageToResetPinCount(@RequestBody CardTokenDto tokenDto, ServletRequest servletRequest) {
        log.info("/pinResetOtp method request: {}", tokenDto);
        try {
            var pinResetOtp = PinResetOtp.builder()
                    .id(1).jsonrpc("2.0").method("card.pin.reset.otp")
                    .params(PinResetOtp.Params.builder().cardId(tokenDto.getToken()).build()).build();
            Object resp = feignService.svgateConnect(pinResetOtp);
            //  cardActionService.createNewAction(pinResetOtp, "card.pin.reset.otp", logProtocolService.addLog(servletRequest).getId());
            log.info("/pinResetOtp method response: {}", resp);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/pinResetVerify")
    public ResponseEntity<?> verifyMessageToResetPinCount(@RequestBody CardVerifyDto cardVerifyDto, ServletRequest servletRequest) {
        log.info("/pinResetVerify method request: {}", cardVerifyDto);
        Random rnd = new Random();
        String stan = String.format("%06d", rnd.nextInt(999999));
        try {
            var pinResetVerify = PinResetVerify.builder()
                    .id(1).jsonrpc("2.0").method("card.pin.reset.verify")
                    .params(PinResetVerify.Params.builder()
                            .card(PinResetVerify.Card.builder()
                                    .cardId(cardVerifyDto.getToken()).merchantId(merchantId)
                                    .terminalId(terminalId).port("9051")
                                    .ext("Card_Verify" + UUID.randomUUID().toString())
                                    .stan(stan).code(cardVerifyDto.getCode()).build()).build()).build();
            Object resp = feignService.svgateConnect(pinResetVerify);
            //  cardActionService.createNewAction(pinResetVerify, "card.pin.reset.verify", logProtocolService.addLog(servletRequest).getId());
            log.info("/pinResetVerify method response: {}", resp);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cardGetBalance")
    public ResponseEntity<?> getBalanceInfoByTokenList(@RequestBody CardTokenListDto tokenDto, ServletRequest servletRequest) {
        log.info("/cardGetBalance method request: {}", tokenDto);
        try {
            var balanceGet = BalanceGet.builder()
                    .id(1).jsonrpc("2.0").method("balance.get")
                    .params(BalanceGet.Params.builder().ids(tokenDto.getToken()).build()).build();
            Object resp = feignService.svgateConnect(balanceGet);
            //   cardActionService.createNewAction(balanceGet, "balance.get", logProtocolService.addLog(servletRequest).getId());
            log.info("/cardGetBalance method response: {}", resp);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cardHistory")
    public ResponseEntity<?> getCardHistoryByToken(@RequestBody CardHistoryDto dto) {
        log.info("/cardHistory method request: {}", dto);
        try {
            var cardHistory = CardHistory.builder()
                    .id("Davr_uzcard_" + UUID.randomUUID()).jsonrpc("2.0").method("trans.history")
                    .params(CardHistory.Params.builder()
                            .criteria(CardHistory.Criteria.builder()
                                    .cardIds(Collections.singletonList(dto.getToken()))
                                    .range(CardHistory.Range.builder().startDate(dto.getStartDate())
                                            .endDate(dto.getEndDate()).build())
                                    .pageNumber(dto.getPageNumber())
                                    .pageSize(dto.getPageSize()).build())
                            .build())
                    .build();
            Object resp = feignService.svgateHistory(cardHistory);
            log.info("/cardHistory method response: {}", resp);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/bins")
    public ResponseEntity<?> getBinsList() {
        try {
            Object response = feignService.svgateBinList(BinList.builder().build());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/transPayPurpose")
    public ResponseEntity<?> debitFromCardByMethodTransPayPurpose(@RequestBody Object dto) {
        log.info("/transPayPurpose method request: {}", dto);
        try {
            Object resp = feignService.svgateTransaction(dto);
            log.info("/transPayPurpose method response: {}", resp);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transPay")
    public ResponseEntity<?> debitFromCardByMethodTransPay(@RequestBody TransPayDto dto) {
        log.info("/transPay method request: {}", dto);
        var transPay = TransPay.builder()
                .id(1)
                .jsonrpc("2.0")
                .method("trans.pay")
                .params(TransPay.Params.builder()
                        .tran(TransPay.Tran.builder()
                                .amount(dto.getAmount())
                                .ext(dto.getExt())
                                .terminalId(dto.getTerminalId())
                                .merchantId(dto.getMerchant())
                                .stan(dto.getStan())
                                .port(dto.getPort())
                                .cardId(dto.getCardId()).build()).build()).build();
        try {
            Object resp = feignService.svgateTransaction(transPay);
            log.info("/transPay method response: {}", resp);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ErrorDto errorDto = new ErrorDto();
            errorDto.setCode("400");
            errorDto.setMessage("Back end Error");
            errorDto.setDetail("Problem with UZCARD");
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/extsv")
    public ResponseEntity<?> showTransactionByExternalId(@RequestParam(value = "ext", required = false) String ext,
                                                         @RequestParam(value = "sv", required = false) String sv) {
        if (ext != null) {
            var externalId = TransExt.builder()
                    .id("123")
                    .jsonrpc("2.0")
                    .method("trans.ext")
                    .params(TransExt.Params.builder()
                            .extId(ext).build()).build();
            log.info("/ext method request: {}", externalId);
            try {
                Object response = feignService.svgateConnect(externalId);
                log.info("/ext method response: {}", response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception ex) {
                log.error(ex.getMessage());
                ErrorDto errorDto = new ErrorDto();
                errorDto.setCode("400");
                errorDto.setMessage("Back end Error");
                errorDto.setDetail("Problem with UZCARD");
                return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
            }
        } else if (sv != null) {
            var utrnno = TransUtrnno.builder()
                    .id("123")
                    .jsonrpc("2.0")
                    .method("trans.sv")
                    .params(TransUtrnno.Params.builder()
                            .svId(sv).build()).build();
            log.info("/sv method request: {}", utrnno);
            try {
                Object response = feignService.svgateConnect(utrnno);
                log.info("/sv method response: {}", response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception ex) {
                log.error(ex.getMessage());
                ErrorDto errorDto = new ErrorDto();
                errorDto.setCode("400");
                errorDto.setMessage("Back end Error");
                errorDto.setDetail("Problem with UZCARD");
                return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("where params?", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/reverseExtSv")
    public ResponseEntity<?> showTransactionByUtrnno(@RequestParam(value = "ext", required = false) String ext,
                                                     @RequestParam(value = "tranId", required = false) String tranId) {
        if (ext != null) {
            var externalRequest = TransExt.builder()
                    .id("123")
                    .jsonrpc("2.0")
                    .method("trans.reverse.ext")
                    .params(TransExt.Params.builder()
                            .extId(ext).build()).build();
            log.info("/reverseExt method request: {}", externalRequest);
            try {
                Object response = feignService.svgateConnect(externalRequest);
                log.info("/reverseExt method response: {}", response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception ex) {
                log.error(ex.getMessage());
                ErrorDto errorDto = new ErrorDto();
                errorDto.setCode("400");
                errorDto.setMessage("Back end Error");
                errorDto.setDetail("Problem with UZCARD");
                return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
            }
        } else if (tranId != null) {
            var tranIdRequest = TransReverse.builder()
                    .id(123)
                    .jsonrpc("2.0")
                    .method("trans.reverse")
                    .params(TransReverse.Params.builder()
                            .tranId(tranId).build()).build();

            log.info("/reverseSv method request: {}", tranIdRequest);
            try {
                Object response = feignService.svgateConnect(tranIdRequest);
                log.info("/reverseSv method response: {}", response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception ex) {
                log.error(ex.getMessage());
                ErrorDto errorDto = new ErrorDto();
                errorDto.setCode("400");
                errorDto.setMessage("Back end Error");
                errorDto.setDetail("Problem with UZCARD");
                return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("where params?", HttpStatus.BAD_REQUEST);
        }

    }


}
