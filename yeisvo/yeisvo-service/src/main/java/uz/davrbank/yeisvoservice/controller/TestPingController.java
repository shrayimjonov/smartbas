package uz.davrbank.yeisvoservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/ping")
@RequiredArgsConstructor
public class TestPingController {

    @GetMapping()
    public ResponseEntity<String> getJuridicMFO(
            @RequestParam("bankId") String bankId,
            @RequestParam("active") Integer active
    ) {
        log.info("request params: {}, {}", bankId, active);
        return ResponseEntity.ok("Ping Starting ....");
    }
}
