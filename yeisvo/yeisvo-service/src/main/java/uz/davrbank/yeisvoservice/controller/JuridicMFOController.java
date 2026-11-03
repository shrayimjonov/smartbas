package uz.davrbank.yeisvoservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.yeisvoservice.aggregate.dto.request.JuridicClientRequest;
import uz.davrbank.yeisvoservice.aggregate.dto.response.ResponseInnDTO;
import uz.davrbank.yeisvoservice.aggregate.dto.response.report.ResponseDTO;
import uz.davrbank.yeisvoservice.service.service.sevices.JuridicMFOService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/inn")
@RequiredArgsConstructor
public class JuridicMFOController {

    private final JuridicMFOService juridicMFOService;

    @GetMapping("/get/inn/list")
    public ResponseEntity<ResponseDTO<List<ResponseInnDTO>>> getJuridicMFO(
            @RequestParam("bankId") String bankId,
            @RequestParam("active") Integer active
    ) {

        return ResponseEntity.ok(juridicMFOService.getJuridicMFO(new JuridicClientRequest(bankId, active)));
    }
}
