package uz.davrbank.service;


import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.request.MessageRequestDto;
import uz.davrbank.dto.response.MessageResponse;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.enums.LanguageType;
import uz.davrbank.enums.SearchType;

import java.util.List;


public interface MessageService {

    ResultData<MessageResponse> createAndPostMessage(MessageRequestDto requestDto);

    ResultData<MessageResponse> createAndPostMessageV2(MessageRequestDto requestDto);

    ResultData<MessageResponse> getMessageById(Long id);

    ResultData<PaginationResponse<MessageResponse>> getAllMessages(PaginationRequest paginationRequest);

    ResultData<MessageResponse> updateMessage(Long id, MessageRequestDto requestDto);

    ResultData<ResponseDto> deleteMessage(Long id);

    ResultData<List<MessageResponse>> searchMessages(LanguageType language, SearchType type, String text);
}
