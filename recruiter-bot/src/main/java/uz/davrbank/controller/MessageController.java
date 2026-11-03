package uz.davrbank.controller;


import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.request.MessageRequestDto;
import uz.davrbank.dto.response.MessageResponse;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.enums.LanguageType;
import uz.davrbank.enums.SearchType;
import uz.davrbank.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/post")
    public ResponseEntity<ResultData<MessageResponse>> postVacancy(@Valid @RequestBody MessageRequestDto requestDto) {
        var result = messageService.createAndPostMessage(requestDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/postWithSchedule")
    public ResponseEntity<ResultData<MessageResponse>> postVacancyV2(@Valid @RequestBody MessageRequestDto requestDto) {
        var result = messageService.createAndPostMessageV2(requestDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultData<MessageResponse>> getMessageById(@PathVariable Long id) {
        var result = messageService.getMessageById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<ResultData<PaginationResponse<MessageResponse>>> getAllMessages(PaginationRequest request) {
        var result = messageService.getAllMessages(request);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResultData<MessageResponse>> updateMessage(@PathVariable Long id, @Valid @RequestBody MessageRequestDto requestDto) {
        var result = messageService.updateMessage(id, requestDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultData<ResponseDto>> deleteMessage(@PathVariable Long id) {
        var result = messageService.deleteMessage(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResultData<List<MessageResponse>> searchMessages(
            @RequestParam(value = "language") LanguageType language,
            @RequestParam(value = "type") SearchType type,
            @RequestParam(value = "text") String text) {
        return messageService.searchMessages(language, type, text);
    }

}