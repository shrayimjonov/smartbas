package uz.davrbank.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.request.MessageRequestDto;
import uz.davrbank.dto.response.MessageResponse;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.Archive;
import uz.davrbank.entity.Message;
import uz.davrbank.entity.PhotoUpload;
import uz.davrbank.enums.LanguageType;
import uz.davrbank.enums.ResponseStatus;
import uz.davrbank.enums.SearchType;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.exception.condition.CustomInternalServerException;
import uz.davrbank.mapper.MessageMapper;
import uz.davrbank.repository.MessageRepository;
import uz.davrbank.repository.specification.MessageSpecification;
import uz.davrbank.service.ArchiveService;
import uz.davrbank.service.MessageService;
import uz.davrbank.service.PhotoUploadService;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@EnableScheduling
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final TelegramService telegramService;
    private final ArchiveService archiveService;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final MessageMapper messageMapper;
    private final String groupChatId;
    private final PhotoUploadService photoUploadService;

    public MessageServiceImpl(@Value("${telegram.group-chat-id}") String groupChatId,
                              MessageRepository messageRepository, TelegramService telegramService,
                              ArchiveService archiveService, ThreadPoolTaskScheduler taskScheduler, MessageMapper messageMapper, PhotoUploadService photoUploadService) {
        this.messageRepository = messageRepository;
        this.telegramService = telegramService;
        this.archiveService = archiveService;
        this.taskScheduler = taskScheduler;
        this.messageMapper = messageMapper;
        this.groupChatId = groupChatId;
        this.photoUploadService = photoUploadService;
    }

    @Override
    @Transactional
    public ResultData<MessageResponse> createAndPostMessage(MessageRequestDto requestDto) {
        String vacancyDescription = Stream.of("uz", "ru", "en")
                .flatMap(lang -> Stream.of(requestDto.getTitle().getText(lang), requestDto.getDescription().getText(lang))
                        .filter(Objects::nonNull)).collect(Collectors.joining("\n\n"));
        Integer messageId = null;
        try {
            messageId = telegramService.sendVacancyPostWithButton(groupChatId, vacancyDescription);
        } catch (TelegramApiException e) {
            log.error("Failed to post message with id {}: {}", messageId, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        requestDto.setMessageId(messageId);
        Message messageEntity = messageMapper.toEntity(requestDto);
        if (requestDto.getPhotoId() != null) {
            messageEntity.setPhotoUpload(photoUploadService.findById(requestDto.getPhotoId()));
        }
        Message savedMessage = messageRepository.save(messageEntity);
        return new ResultData<>(messageMapper.toDto(savedMessage));
    }

    @Override
    public ResultData<MessageResponse> getMessageById(Long id) {
        Message message = getMessage(id);
        return new ResultData<>(messageMapper.toDto(message));
    }


    @Override
    public ResultData<PaginationResponse<MessageResponse>> getAllMessages(PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        Page<Message> messagePage = messageRepository.findAll(MessageSpecification.orderByIdDesc(), pageable);
        PaginationResponse<MessageResponse> paginationResponse = PaginationResponse.fromPage(messagePage, messageMapper::toDto);
        return new ResultData<>(paginationResponse);
    }


    @Override
    @Transactional
    public ResultData<MessageResponse> updateMessage(Long id, MessageRequestDto requestDto) {
        Message existingMessage = getMessage(id);
        Integer messageId = existingMessage.getMessageId();
        requestDto.setMessageId(messageId);
        Message updatedMessage = messageMapper.toEntity(requestDto);
        updatedMessage.setId(id);
        if (requestDto.getPhotoId() != null) {
            updatedMessage.setPhotoUpload(photoUploadService.findById(requestDto.getPhotoId()));
        }
        messageRepository.save(updatedMessage);
        if (messageId != null) {
            try {
                telegramService.updateMessageInGroup(groupChatId, messageId, requestDto);
            } catch (TelegramApiException e) {
                log.error("Failed to update message with id {}: {}", messageId, e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        return new ResultData<>(messageMapper.toDto(updatedMessage));
    }

    @Override
    @Transactional
    public ResultData<ResponseDto> deleteMessage(Long id) {
        Message message = getMessage(id);
        log.info("Deleting message with id {} and telegram messageId {}", id, message.getMessageId());
        if (message.getMessageId() != null) {
            try {
                telegramService.deleteMessageFromGroup(groupChatId, message.getMessageId());
            } catch (TelegramApiException e) {
                log.error("Failed to delete message with id {}: {}", id, e.getMessage());
                throw new CustomInternalServerException(e.getMessage());
            }
        }
        Archive archive = Archive.builder().title(message.getTitle()).description(message.getDescription()).deletedDate(LocalDateTime.now()).messageId(message.getMessageId()).build();
        log.info("Archiving message with id {}", id);
        archiveService.saveArchive(archive);
        messageRepository.deleteById(id);
        ResponseDto responseDto = ResponseDto.builder().code(ResponseStatus.SUCCESS.getCode()).message(ResponseStatus.SUCCESS.getMessage()).build();
        return new ResultData<>(responseDto);
    }

    @Override
    public ResultData<List<MessageResponse>> searchMessages(LanguageType language, SearchType type, String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ResultData<>(false, ResponseStatus.TEXT_DATA, "Text should not be empty.");
        }
        Specification<Message> specification = MessageSpecification.searchByLanguageAndType(language.getCode(), type.getType(), text);
        List<Message> messages = messageRepository.findAll(specification);
        List<MessageResponse> responseList = messageMapper.toDtoList(messages);
        if (responseList.isEmpty()) {
            return new ResultData<>(false, ResponseStatus.INCORRECT_DATA, "No matching messages found.");
        }
        return new ResultData<>(responseList);
    }

    @Override
    @Transactional
    public ResultData<MessageResponse> createAndPostMessageV2(MessageRequestDto dto) {
        dto.setCreatedAt(LocalDateTime.now());
        String fullText = buildLocalizedMessage(dto);
        Message message = messageMapper.toEntity(dto);
        if (dto.getPhotoId() != null) {
            message.setPhotoUpload(photoUploadService.findById(dto.getPhotoId()));
        }
        Message saved = messageRepository.save(message);
        if (StringUtils.hasText(dto.getSendTime())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime sendAt = LocalDateTime.parse(dto.getSendTime(), formatter);
            if (sendAt.isAfter(LocalDateTime.now())) {
                long delayMs = Duration.between(LocalDateTime.now(), sendAt).toMillis();
                taskScheduler.schedule(
                        () -> postMessage(saved, dto, fullText),
                        Instant.now().plusMillis(delayMs)
                );
                return new ResultData<>(true, ResponseStatus.SCHEDULED, "Message scheduled successfully");
            }
            return new ResultData<>(false, ResponseStatus.FAILED, "Scheduled time is in the past");
        }
        return postMessage(saved, dto, fullText);
    }


    private String buildLocalizedMessage(MessageRequestDto dto) {
        StringBuilder sb = new StringBuilder();
        Stream.of("uz", "ru", "en").forEach(lang -> {
            boolean added = false;
            if (dto.getTitle() != null && dto.getTitle().getText(lang) != null) {
                sb.append(dto.getTitle().getText(lang)).append("\n\n");
                added = true;
            }
            if (dto.getDescription() != null && dto.getDescription().getText(lang) != null) {
                sb.append(dto.getDescription().getText(lang)).append("\n\n");
                added = true;
            }
            if (added && !"en".equals(lang)) {
                sb.append("\n");
            }
        });
        int len = sb.length();
        if (len >= 2) sb.setLength(len - 2);
        return sb.toString();
    }

    @Transactional
    protected ResultData<MessageResponse> postMessage(Message message, MessageRequestDto dto, String fullText) {
        InputFile photoFile = resolvePhotoFile(dto.getPhotoId());
        var markup = telegramService.buildApplyButton();
        Integer telegramMsgId;
        try {
            telegramMsgId = photoFile != null
                    ? telegramService.sendPhoto(groupChatId, photoFile, fullText, markup)
                    : telegramService.sendMessage(groupChatId, fullText, markup);
        } catch (TelegramApiException e) {
            log.error("Failed to post message with id {}: {}", message.getId(), e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        if (telegramMsgId == null) {
            return new ResultData<>(false, ResponseStatus.FAILED, "Failed to post message to Telegram");
        }
        message.setMessageId(telegramMsgId);
        Message updated = messageRepository.save(message);
        return new ResultData<>(messageMapper.toDto(updated));
    }

    private InputFile resolvePhotoFile(Long photoId) {
        if (photoId == null) return null;
        PhotoUpload photoUpload = photoUploadService.findById(photoId);
        if (isImageFile(photoUpload.getPhotoName())) {
            return new InputFile(new ByteArrayInputStream(photoUpload.getPhoto()), photoUpload.getPhotoName());
        }
        throw new IllegalArgumentException("Invalid photoId: " + photoId);
    }

    private boolean isImageFile(String name) {
        return name != null && name.toLowerCase().matches(".*\\.(jpg|jpeg|png)");
    }

    private Message getMessage(Long id) {
        return messageRepository.findById(id).orElseThrow(() -> new NotFoundException("Message not found with id: " + id));
    }
}