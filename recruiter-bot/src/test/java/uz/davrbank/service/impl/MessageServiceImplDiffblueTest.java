package uz.davrbank.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup.InlineKeyboardMarkupBuilder;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.davrbank.dto.pagination.PaginationRequest;
import uz.davrbank.dto.pagination.PaginationResponse;
import uz.davrbank.dto.request.LocalizedTextDto;
import uz.davrbank.dto.request.MessageRequestDto;
import uz.davrbank.dto.request.MessageRequestDto.MessageRequestDtoBuilder;
import uz.davrbank.dto.response.MessageResponse;
import uz.davrbank.dto.response.MessageResponse.MessageResponseBuilder;
import uz.davrbank.dto.response.ResponseData;
import uz.davrbank.dto.response.ResponseData.Error;
import uz.davrbank.dto.response.ResponseDto;
import uz.davrbank.dto.response.ResultData;
import uz.davrbank.entity.Archive;
import uz.davrbank.entity.LocalizedText;
import uz.davrbank.entity.Message;
import uz.davrbank.entity.PhotoUpload;
import uz.davrbank.exception.NotFoundException;
import uz.davrbank.mapper.ArchiveMapperImpl;
import uz.davrbank.mapper.MessageMapper;
import uz.davrbank.mapper.MessageMapperImpl;
import uz.davrbank.mapper.PhotoUploadMapper;
import uz.davrbank.repository.ArchiveRepository;
import uz.davrbank.repository.MessageRepository;
import uz.davrbank.repository.PhotoUploadRepository;
import uz.davrbank.service.ArchiveService;
import uz.davrbank.service.PhotoUploadService;

@ContextConfiguration(
        classes = {MessageServiceImpl.class, String.class, ThreadPoolTaskScheduler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class MessageServiceImplDiffblueTest {
    @MockitoBean
    private ArchiveService archiveService;

    @MockitoBean
    private MessageMapper messageMapper;

    @MockitoBean
    private MessageRepository messageRepository;

    @Autowired
    private MessageServiceImpl messageServiceImpl;

    @MockitoBean
    private PhotoUploadService photoUploadService;

    @MockitoBean
    private TelegramService telegramService;

    /**
     * Test {@link MessageServiceImpl#createAndPostMessage(MessageRequestDto)}.
     *
     * <ul>
     *   <li>Then return Data PhotoId is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#createAndPostMessage(MessageRequestDto)}
     */
    @Test
    @DisplayName("Test createAndPostMessage(MessageRequestDto); then return Data PhotoId is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.createAndPostMessage(MessageRequestDto)"})
    void testCreateAndPostMessage_thenReturnDataPhotoIdIsNull()
            throws UnknownHostException, TelegramApiException {
        // Arrange
        try (MockedStatic<InetAddress> mockInetAddress = mockStatic(InetAddress.class)) {
            mockInetAddress
                    .when(() -> InetAddress.getAllByName(Mockito.<String>any()))
                    .thenThrow(new IllegalArgumentException());

            LocalizedText description = new LocalizedText();
            description.setEn("En");
            description.setRu("Ru");
            description.setUz("Uz");

            LocalizedText title = new LocalizedText();
            title.setEn("En");
            title.setRu("Ru");
            title.setUz("Uz");

            Message message = new Message();
            message.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
            message.setDescription(description);
            message.setId(1L);
            message.setMessageId(1);
            message.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
            message.setTitle(title);

            MessageRepository messageRepository = mock(MessageRepository.class);
            when(messageRepository.save(Mockito.<Message>any())).thenReturn(message);

            TelegramService telegramService = mock(TelegramService.class);
            when(telegramService.sendVacancyPostWithButton(Mockito.<String>any(), Mockito.<String>any()))
                    .thenReturn(3);
            ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
            ArchiveServiceImpl archiveService =
                    new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());
            ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
            MessageMapperImpl messageMapper = new MessageMapperImpl();
            PhotoUploadServiceImpl photoUploadService =
                    new PhotoUploadServiceImpl(
                            mock(PhotoUploadRepository.class), mock(PhotoUploadMapper.class));

            MessageServiceImpl messageServiceImpl =
                    new MessageServiceImpl(
                            "42",
                            messageRepository,
                            telegramService,
                            archiveService,
                            taskScheduler,
                            messageMapper,
                            photoUploadService);

            MessageRequestDtoBuilder createdAtResult =
                    MessageRequestDto.builder().createdAt(LocalDate.of(1970, 1, 1).atStartOfDay());

            MessageRequestDtoBuilder sendTimeResult =
                    createdAtResult
                            .description(LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build())
                            .messageId(1)
                            .sendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                            .sendTime("Send Time");
            MessageRequestDto requestDto =
                    sendTimeResult
                            .title(LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build())
                            .build();

            // Act
            ResultData<MessageResponse> actualCreateAndPostMessageResult =
                    messageServiceImpl.createAndPostMessage(requestDto);

            // Assert
            verify(messageRepository).save(isA(Message.class));
            verify(telegramService).sendVacancyPostWithButton("42", "Uz\n\nUz\n\nRu\n\nRu\n\nEn\n\nEn");
            MessageResponse data = actualCreateAndPostMessageResult.getData();
            assertNull(data.getPhotoId());
            assertNull(data.getCreatedAt());
            assertNull(actualCreateAndPostMessageResult.getError());
            assertEquals(1, data.getMessageId().intValue());
            assertEquals(1L, data.getId().longValue());
            assertEquals(200, actualCreateAndPostMessageResult.getCode().intValue());
            assertEquals(3, requestDto.getMessageId().intValue());
            assertTrue(actualCreateAndPostMessageResult.isSuccess());
            assertSame(description, data.getDescription());
            assertSame(title, data.getTitle());
        }
    }

    /**
     * Test {@link MessageServiceImpl#createAndPostMessage(MessageRequestDto)}.
     *
     * <ul>
     *   <li>Then throw {@link IllegalArgumentException}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#createAndPostMessage(MessageRequestDto)}
     */
    @Test
    @DisplayName("Test createAndPostMessage(MessageRequestDto); then throw IllegalArgumentException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.createAndPostMessage(MessageRequestDto)"})
    void testCreateAndPostMessage_thenThrowIllegalArgumentException()
            throws UnknownHostException, TelegramApiException {
        // Arrange
        try (MockedStatic<InetAddress> mockInetAddress = mockStatic(InetAddress.class)) {
            mockInetAddress
                    .when(() -> InetAddress.getAllByName(Mockito.<String>any()))
                    .thenThrow(new IllegalArgumentException());

            MessageRepository messageRepository = mock(MessageRepository.class);
            when(messageRepository.save(Mockito.<Message>any()))
                    .thenThrow(new IllegalArgumentException());

            TelegramService telegramService = mock(TelegramService.class);
            when(telegramService.sendVacancyPostWithButton(Mockito.<String>any(), Mockito.<String>any()))
                    .thenReturn(3);
            ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
            ArchiveServiceImpl archiveService =
                    new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());
            ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
            MessageMapperImpl messageMapper = new MessageMapperImpl();
            PhotoUploadServiceImpl photoUploadService =
                    new PhotoUploadServiceImpl(
                            mock(PhotoUploadRepository.class), mock(PhotoUploadMapper.class));

            MessageServiceImpl messageServiceImpl =
                    new MessageServiceImpl(
                            "42",
                            messageRepository,
                            telegramService,
                            archiveService,
                            taskScheduler,
                            messageMapper,
                            photoUploadService);

            MessageRequestDtoBuilder createdAtResult =
                    MessageRequestDto.builder().createdAt(LocalDate.of(1970, 1, 1).atStartOfDay());

            MessageRequestDtoBuilder sendTimeResult =
                    createdAtResult
                            .description(LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build())
                            .messageId(1)
                            .sendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                            .sendTime("Send Time");

            // Act and Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () ->
                            messageServiceImpl.createAndPostMessage(
                                    sendTimeResult
                                            .title(LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build())
                                            .build()));
            verify(messageRepository).save(isA(Message.class));
            verify(telegramService).sendVacancyPostWithButton("42", "Uz\n\nUz\n\nRu\n\nRu\n\nEn\n\nEn");
        }
    }

    /**
     * Test {@link MessageServiceImpl#createAndPostMessage(MessageRequestDto)}.
     *
     * <ul>
     *   <li>Then throw {@link NotFoundException}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#createAndPostMessage(MessageRequestDto)}
     */
    @Test
    @DisplayName("Test createAndPostMessage(MessageRequestDto); then throw NotFoundException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.createAndPostMessage(MessageRequestDto)"})
    void testCreateAndPostMessage_thenThrowNotFoundException()
            throws UnknownHostException, TelegramApiException {
        // Arrange
        try (MockedStatic<InetAddress> mockInetAddress = mockStatic(InetAddress.class)) {
            mockInetAddress
                    .when(() -> InetAddress.getAllByName(Mockito.<String>any()))
                    .thenThrow(new IllegalArgumentException());

            MessageRepository messageRepository = mock(MessageRepository.class);
            when(messageRepository.save(Mockito.<Message>any())).thenThrow(new NotFoundException());

            TelegramService telegramService = mock(TelegramService.class);
            when(telegramService.sendVacancyPostWithButton(Mockito.<String>any(), Mockito.<String>any()))
                    .thenReturn(3);

            LocalizedText description = new LocalizedText();
            description.setEn("En");
            description.setRu("Ru");
            description.setUz("Uz");

            LocalizedText title = new LocalizedText();
            title.setEn("En");
            title.setRu("Ru");
            title.setUz("Uz");

            Message message = new Message();
            message.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
            message.setDescription(description);
            message.setId(1L);
            message.setMessageId(1);
            message.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
            message.setTitle(title);

            MessageMapperImpl messageMapper = mock(MessageMapperImpl.class);
            when(messageMapper.toEntity(Mockito.<MessageRequestDto>any())).thenReturn(message);
            ArchiveRepository archiveRepository = mock(ArchiveRepository.class);
            ArchiveServiceImpl archiveService =
                    new ArchiveServiceImpl(archiveRepository, new ArchiveMapperImpl());
            ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
            PhotoUploadServiceImpl photoUploadService =
                    new PhotoUploadServiceImpl(
                            mock(PhotoUploadRepository.class), mock(PhotoUploadMapper.class));

            MessageServiceImpl messageServiceImpl =
                    new MessageServiceImpl(
                            "42",
                            messageRepository,
                            telegramService,
                            archiveService,
                            taskScheduler,
                            messageMapper,
                            photoUploadService);

            MessageRequestDtoBuilder createdAtResult =
                    MessageRequestDto.builder().createdAt(LocalDate.of(1970, 1, 1).atStartOfDay());

            MessageRequestDtoBuilder sendTimeResult =
                    createdAtResult
                            .description(LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build())
                            .messageId(1)
                            .sendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                            .sendTime("Send Time");

            // Act and Assert
            assertThrows(
                    NotFoundException.class,
                    () ->
                            messageServiceImpl.createAndPostMessage(
                                    sendTimeResult
                                            .title(LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build())
                                            .build()));
            verify(messageRepository).save(isA(Message.class));
            verify(messageMapper).toEntity(isA(MessageRequestDto.class));
            verify(telegramService).sendVacancyPostWithButton("42", "Uz\n\nUz\n\nRu\n\nRu\n\nEn\n\nEn");
        }
    }

    /**
     * Test {@link MessageServiceImpl#getAllMessages(PaginationRequest)}.
     *
     * <ul>
     *   <li>Given {@code null}.
     *   <li>Then calls {@link PaginationRequest#toPageable()}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#getAllMessages(PaginationRequest)}
     */
    @Test
    @DisplayName("Test getAllMessages(PaginationRequest); given 'null'; then calls toPageable()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.getAllMessages(PaginationRequest)"})
    void testGetAllMessages_givenNull_thenCallsToPageable() {
        // Arrange
        when(messageRepository.findAll(Mockito.<Specification<Message>>any(), Mockito.<Pageable>any()))
                .thenThrow(new IllegalArgumentException());

        PaginationRequest paginationRequest = mock(PaginationRequest.class);
        when(paginationRequest.toPageable()).thenReturn(null);

        // Act and Assert
        assertThrows(
                IllegalArgumentException.class, () -> messageServiceImpl.getAllMessages(paginationRequest));
        verify(messageRepository).findAll(isA(Specification.class), (Pageable) isNull());
        verify(paginationRequest).toPageable();
    }

    /**
     * Test {@link MessageServiceImpl#getAllMessages(PaginationRequest)}.
     *
     * <ul>
     *   <li>Then return Error is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#getAllMessages(PaginationRequest)}
     */
    @Test
    @DisplayName("Test getAllMessages(PaginationRequest); then return Error is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.getAllMessages(PaginationRequest)"})
    void testGetAllMessages_thenReturnErrorIsNull() {
        // Arrange
        when(messageRepository.findAll(Mockito.<Specification<Message>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act
        ResultData<PaginationResponse<MessageResponse>> actualAllMessages =
                messageServiceImpl.getAllMessages(new PaginationRequest());

        // Assert
        verify(messageRepository).findAll(isA(Specification.class), isA(Pageable.class));
        assertNull(actualAllMessages.getError());
        PaginationResponse<MessageResponse> data = actualAllMessages.getData();
        assertEquals(0, data.getPage());
        assertEquals(0, data.getSize());
        assertEquals(0L, data.getTotalElements());
        assertEquals(1, data.getTotalPages());
        assertEquals(200, actualAllMessages.getCode().intValue());
        assertTrue(data.getContent().isEmpty());
        assertTrue(actualAllMessages.isSuccess());
    }

    /**
     * Test {@link MessageServiceImpl#getAllMessages(PaginationRequest)}.
     *
     * <ul>
     *   <li>Then throw {@link IllegalArgumentException}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#getAllMessages(PaginationRequest)}
     */
    @Test
    @DisplayName("Test getAllMessages(PaginationRequest); then throw IllegalArgumentException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.getAllMessages(PaginationRequest)"})
    void testGetAllMessages_thenThrowIllegalArgumentException() {
        // Arrange
        when(messageRepository.findAll(Mockito.<Specification<Message>>any(), Mockito.<Pageable>any()))
                .thenThrow(new IllegalArgumentException());

        // Act and Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> messageServiceImpl.getAllMessages(new PaginationRequest()));
        verify(messageRepository).findAll(isA(Specification.class), isA(Pageable.class));
    }

    /**
     * Test {@link MessageServiceImpl#deleteMessage(Long)}.
     *
     * <p>Method under test: {@link MessageServiceImpl#deleteMessage(Long)}
     */
    @Test
    @DisplayName("Test deleteMessage(Long)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.deleteMessage(Long)"})
    void testDeleteMessage() throws TelegramApiException {
        // Arrange
        LocalizedText description = new LocalizedText();
        description.setEn("En");
        description.setRu("Ru");
        description.setUz("Uz");

        LocalizedText title = new LocalizedText();
        title.setEn("En");
        title.setRu("Ru");
        title.setUz("Uz");

        Message message = new Message();
        message.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setDescription(description);
        message.setId(1L);
        message.setMessageId(1);
        message.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setTitle(title);
        Optional<Message> ofResult = Optional.of(message);
        when(messageRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doThrow(new IllegalArgumentException())
                .when(telegramService)
                .deleteMessageFromGroup(Mockito.<String>any(), Mockito.<Integer>any());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> messageServiceImpl.deleteMessage(1L));
        verify(messageRepository).findById(1L);
        verify(telegramService).deleteMessageFromGroup("${telegram.group-chat-id}", 1);
    }

    /**
     * Test {@link MessageServiceImpl#deleteMessage(Long)}.
     *
     * <ul>
     *   <li>Given {@link ArchiveService} {@link ArchiveService#saveArchive(Archive)} throw {@link
     *       IllegalArgumentException#IllegalArgumentException()}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#deleteMessage(Long)}
     */
    @Test
    @DisplayName(
            "Test deleteMessage(Long); given ArchiveService saveArchive(Archive) throw IllegalArgumentException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.deleteMessage(Long)"})
    void testDeleteMessage_givenArchiveServiceSaveArchiveThrowIllegalArgumentException()
            throws TelegramApiException {
        // Arrange
        LocalizedText description = new LocalizedText();
        description.setEn("En");
        description.setRu("Ru");
        description.setUz("Uz");

        LocalizedText title = new LocalizedText();
        title.setEn("En");
        title.setRu("Ru");
        title.setUz("Uz");

        Message message = new Message();
        message.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setDescription(description);
        message.setId(1L);
        message.setMessageId(1);
        message.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setTitle(title);
        Optional<Message> ofResult = Optional.of(message);
        when(messageRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing()
                .when(telegramService)
                .deleteMessageFromGroup(Mockito.<String>any(), Mockito.<Integer>any());
        doThrow(new IllegalArgumentException())
                .when(archiveService)
                .saveArchive(Mockito.<Archive>any());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> messageServiceImpl.deleteMessage(1L));
        verify(messageRepository).findById(1L);
        verify(archiveService).saveArchive(isA(Archive.class));
        verify(telegramService).deleteMessageFromGroup("${telegram.group-chat-id}", 1);
    }

    /**
     * Test {@link MessageServiceImpl#deleteMessage(Long)}.
     *
     * <ul>
     *   <li>Given {@link MessageRepository} {@link MessageRepository#findById(Object)} throw {@link
     *       IllegalArgumentException#IllegalArgumentException()}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#deleteMessage(Long)}
     */
    @Test
    @DisplayName(
            "Test deleteMessage(Long); given MessageRepository findById(Object) throw IllegalArgumentException()")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.deleteMessage(Long)"})
    void testDeleteMessage_givenMessageRepositoryFindByIdThrowIllegalArgumentException()
            throws TelegramApiException {
        // Arrange
        when(messageRepository.findById(Mockito.<Long>any())).thenThrow(new IllegalArgumentException());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> messageServiceImpl.deleteMessage(1L));
        verify(messageRepository).findById(1L);
    }

    /**
     * Test {@link MessageServiceImpl#deleteMessage(Long)}.
     *
     * <ul>
     *   <li>Then return Data Message is {@code SUCCESS}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#deleteMessage(Long)}
     */
    @Test
    @DisplayName("Test deleteMessage(Long); then return Data Message is 'SUCCESS'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.deleteMessage(Long)"})
    void testDeleteMessage_thenReturnDataMessageIsSuccess() throws TelegramApiException {
        // Arrange
        LocalizedText description = new LocalizedText();
        description.setEn("En");
        description.setRu("Ru");
        description.setUz("Uz");

        LocalizedText title = new LocalizedText();
        title.setEn("En");
        title.setRu("Ru");
        title.setUz("Uz");

        Message message = new Message();
        message.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setDescription(description);
        message.setId(1L);
        message.setMessageId(1);
        message.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setTitle(title);
        Optional<Message> ofResult = Optional.of(message);
        doNothing().when(messageRepository).deleteById(Mockito.<Long>any());
        when(messageRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing()
                .when(telegramService)
                .deleteMessageFromGroup(Mockito.<String>any(), Mockito.<Integer>any());
        doNothing().when(archiveService).saveArchive(Mockito.<Archive>any());

        // Act
        ResultData<ResponseDto> actualDeleteMessageResult = messageServiceImpl.deleteMessage(1L);

        // Assert
        verify(messageRepository).deleteById(1L);
        verify(messageRepository).findById(1L);
        verify(archiveService).saveArchive(isA(Archive.class));
        verify(telegramService).deleteMessageFromGroup("${telegram.group-chat-id}", 1);
        ResponseDto data = actualDeleteMessageResult.getData();
        assertEquals("SUCCESS", data.getMessage());
        assertNull(actualDeleteMessageResult.getError());
        assertEquals(200, actualDeleteMessageResult.getCode().intValue());
        assertEquals(200, data.getCode().intValue());
        assertTrue(actualDeleteMessageResult.isSuccess());
    }

    /**
     * Test {@link MessageServiceImpl#deleteMessage(Long)}.
     *
     * <ul>
     *   <li>Then throw {@link NotFoundException}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#deleteMessage(Long)}
     */
    @Test
    @DisplayName("Test deleteMessage(Long); then throw NotFoundException")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.deleteMessage(Long)"})
    void testDeleteMessage_thenThrowNotFoundException() throws TelegramApiException {
        // Arrange
        Optional<Message> emptyResult = Optional.empty();
        when(messageRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> messageServiceImpl.deleteMessage(1L));
        verify(messageRepository).findById(1L);
    }

    /**
     * Test {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}.
     *
     * <p>Method under test: {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}
     */
    @Test
    @DisplayName("Test createAndPostMessageV2(MessageRequestDto)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.createAndPostMessageV2(MessageRequestDto)"})
    void testCreateAndPostMessageV2() throws TelegramApiException {
        // Arrange
        LocalizedText description = new LocalizedText();
        description.setEn("En");
        description.setRu("Ru");
        description.setUz("Uz");

        LocalizedText title = new LocalizedText();
        title.setEn("En");
        title.setRu("Ru");
        title.setUz("Uz");

        Message message = new Message();
        message.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setDescription(description);
        message.setId(1L);
        message.setMessageId(1);
        message.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setTitle(title);
        when(messageRepository.save(Mockito.<Message>any())).thenReturn(message);
        when(telegramService.buildApplyButton()).thenThrow(new IllegalArgumentException());

        LocalizedText description2 = new LocalizedText();
        description2.setEn("En");
        description2.setRu("Ru");
        description2.setUz("Uz");

        LocalizedText title2 = new LocalizedText();
        title2.setEn("En");
        title2.setRu("Ru");
        title2.setUz("Uz");

        Message message2 = new Message();
        message2.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message2.setDescription(description2);
        message2.setId(1L);
        message2.setMessageId(1);
        message2.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message2.setTitle(title2);
        when(messageMapper.toEntity(Mockito.<MessageRequestDto>any())).thenReturn(message2);

        // Act and Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> messageServiceImpl.createAndPostMessageV2(new MessageRequestDto()));
        verify(messageRepository).save(isA(Message.class));
        verify(messageMapper).toEntity(isA(MessageRequestDto.class));
        verify(telegramService).buildApplyButton();
    }

    /**
     * Test {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}.
     *
     * <ul>
     *   <li>Given {@link MessageRepository}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}
     */
    @Test
    @DisplayName("Test createAndPostMessageV2(MessageRequestDto); given MessageRepository")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.createAndPostMessageV2(MessageRequestDto)"})
    void testCreateAndPostMessageV2_givenMessageRepository() throws TelegramApiException {
        // Arrange
        when(messageMapper.toEntity(Mockito.<MessageRequestDto>any()))
                .thenThrow(new IllegalArgumentException());

        // Act and Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> messageServiceImpl.createAndPostMessageV2(new MessageRequestDto()));
        verify(messageMapper).toEntity(isA(MessageRequestDto.class));
    }

    /**
     * Test {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}.
     *
     * <ul>
     *   <li>Given {@link PhotoUpload#PhotoUpload()} Id is one.
     *   <li>Then calls {@link PhotoUploadService#findById(Long)}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}
     */
    @Test
    @DisplayName(
            "Test createAndPostMessageV2(MessageRequestDto); given PhotoUpload() Id is one; then calls findById(Long)")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.createAndPostMessageV2(MessageRequestDto)"})
    void testCreateAndPostMessageV2_givenPhotoUploadIdIsOne_thenCallsFindById()
            throws UnsupportedEncodingException, TelegramApiException {
        // Arrange
        LocalizedText description = new LocalizedText();
        description.setEn("En");
        description.setRu("Ru");
        description.setUz("Uz");

        LocalizedText title = new LocalizedText();
        title.setEn("En");
        title.setRu("Ru");
        title.setUz("Uz");

        Message message = new Message();
        message.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setDescription(description);
        message.setId(1L);
        message.setMessageId(1);
        message.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setTitle(title);
        when(messageRepository.save(Mockito.<Message>any())).thenReturn(message);

        LocalizedText description2 = new LocalizedText();
        description2.setEn("En");
        description2.setRu("Ru");
        description2.setUz("Uz");

        LocalizedText title2 = new LocalizedText();
        title2.setEn("En");
        title2.setRu("Ru");
        title2.setUz("Uz");

        Message message2 = new Message();
        message2.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message2.setDescription(description2);
        message2.setId(1L);
        message2.setMessageId(1);
        message2.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message2.setTitle(title2);
        when(messageMapper.toEntity(Mockito.<MessageRequestDto>any())).thenReturn(message2);

        PhotoUpload photoUpload = new PhotoUpload();
        photoUpload.setId(1L);
        photoUpload.setPhoto("AXAXAXAX".getBytes("UTF-8"));
        photoUpload.setUploadedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(photoUploadService.findById(Mockito.<Long>any())).thenReturn(photoUpload);
        LocalizedTextDto title3 = LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build();
        LocalizedTextDto description3 = LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build();

        MessageRequestDto dto =
                new MessageRequestDto(
                        title3,
                        description3,
                        LocalDate.of(1970, 1, 1).atStartOfDay(),
                        LocalDate.of(1970, 1, 1).atStartOfDay(),
                        1,
                        null,
                        1L);

        // Act and Assert
        assertThrows(
                IllegalArgumentException.class, () -> messageServiceImpl.createAndPostMessageV2(dto));
        verify(messageRepository).save(isA(Message.class));
        verify(messageMapper).toEntity(isA(MessageRequestDto.class));
        verify(photoUploadService).findById(1L);
    }

    /**
     * Test {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}.
     *
     * <ul>
     *   <li>Then return Data PhotoId is {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}
     */
    @Test
    @DisplayName("Test createAndPostMessageV2(MessageRequestDto); then return Data PhotoId is 'null'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.createAndPostMessageV2(MessageRequestDto)"})
    void testCreateAndPostMessageV2_thenReturnDataPhotoIdIsNull() throws TelegramApiException {
        // Arrange
        LocalizedText description = new LocalizedText();
        description.setEn("En");
        description.setRu("Ru");
        description.setUz("Uz");

        LocalizedText title = new LocalizedText();
        title.setEn("En");
        title.setRu("Ru");
        title.setUz("Uz");

        Message message = new Message();
        message.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setDescription(description);
        message.setId(1L);
        message.setMessageId(1);
        message.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setTitle(title);
        when(messageRepository.save(Mockito.<Message>any())).thenReturn(message);

        InlineKeyboardMarkupBuilder builderResult = InlineKeyboardMarkup.builder();
        when(telegramService.buildApplyButton())
                .thenReturn(builderResult.keyboard(new ArrayList<>()).build());
        when(telegramService.sendMessage(
                Mockito.<String>any(), Mockito.<String>any(), Mockito.<InlineKeyboardMarkup>any()))
                .thenReturn(3);

        LocalizedText description2 = new LocalizedText();
        description2.setEn("En");
        description2.setRu("Ru");
        description2.setUz("Uz");

        MessageResponseBuilder sendPostDateResult =
                MessageResponse.builder()
                        .createdAt(LocalDate.of(1970, 1, 1).atStartOfDay())
                        .description(description2)
                        .id(1L)
                        .messageId(1)
                        .sendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());

        LocalizedText title2 = new LocalizedText();
        title2.setEn("En");
        title2.setRu("Ru");
        title2.setUz("Uz");
        MessageResponse messageResponse = sendPostDateResult.title(title2).build();

        LocalizedText description3 = new LocalizedText();
        description3.setEn("En");
        description3.setRu("Ru");
        description3.setUz("Uz");

        LocalizedText title3 = new LocalizedText();
        title3.setEn("En");
        title3.setRu("Ru");
        title3.setUz("Uz");

        Message message2 = new Message();
        message2.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message2.setDescription(description3);
        message2.setId(1L);
        message2.setMessageId(1);
        message2.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message2.setTitle(title3);
        when(messageMapper.toDto(Mockito.<Message>any())).thenReturn(messageResponse);
        when(messageMapper.toEntity(Mockito.<MessageRequestDto>any())).thenReturn(message2);

        // Act
        ResultData<MessageResponse> actualCreateAndPostMessageV2Result =
                messageServiceImpl.createAndPostMessageV2(new MessageRequestDto());

        // Assert
        verify(messageRepository, atLeast(1)).save(Mockito.<Message>any());
        verify(messageMapper).toDto(isA(Message.class));
        verify(messageMapper).toEntity(isA(MessageRequestDto.class));
        verify(telegramService).buildApplyButton();
        verify(telegramService)
                .sendMessage(eq("${telegram.group-chat-id}"), eq(""), isA(InlineKeyboardMarkup.class));
        MessageResponse data = actualCreateAndPostMessageV2Result.getData();
        assertNull(data.getPhotoId());
        assertNull(actualCreateAndPostMessageV2Result.getError());
        assertEquals(1, data.getMessageId().intValue());
        assertEquals(1L, data.getId().longValue());
        assertEquals(200, actualCreateAndPostMessageV2Result.getCode().intValue());
        assertTrue(actualCreateAndPostMessageV2Result.isSuccess());
        assertSame(description2, data.getDescription());
        assertSame(title2, data.getTitle());
    }

    /**
     * Test {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}.
     *
     * <ul>
     *   <li>Then return Error Error is {@code Invalid send time format}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}
     */
    @Test
    @DisplayName(
            "Test createAndPostMessageV2(MessageRequestDto); then return Error Error is 'Invalid send time format'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.createAndPostMessageV2(MessageRequestDto)"})
    void testCreateAndPostMessageV2_thenReturnErrorErrorIsInvalidSendTimeFormat()
            throws TelegramApiException {
        // Arrange
        LocalizedText description = new LocalizedText();
        description.setEn("En");
        description.setRu("Ru");
        description.setUz("Uz");

        LocalizedText title = new LocalizedText();
        title.setEn("En");
        title.setRu("Ru");
        title.setUz("Uz");

        Message message = new Message();
        message.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setDescription(description);
        message.setId(1L);
        message.setMessageId(1);
        message.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setTitle(title);
        when(messageRepository.save(Mockito.<Message>any())).thenReturn(message);

        LocalizedText description2 = new LocalizedText();
        description2.setEn("En");
        description2.setRu("Ru");
        description2.setUz("Uz");

        LocalizedText title2 = new LocalizedText();
        title2.setEn("En");
        title2.setRu("Ru");
        title2.setUz("Uz");

        Message message2 = new Message();
        message2.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message2.setDescription(description2);
        message2.setId(1L);
        message2.setMessageId(1);
        message2.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message2.setTitle(title2);
        when(messageMapper.toEntity(Mockito.<MessageRequestDto>any())).thenReturn(message2);
        LocalizedTextDto title3 = LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build();
        LocalizedTextDto description3 = LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build();

        MessageRequestDto dto =
                new MessageRequestDto(
                        title3, description3, LocalDate.of(1970, 1, 1).atStartOfDay(), null, 1, "uz", 1L);

        // Act
        ResultData<MessageResponse> actualCreateAndPostMessageV2Result =
                messageServiceImpl.createAndPostMessageV2(dto);

        // Assert
        verify(messageRepository).save(isA(Message.class));
        verify(messageMapper).toEntity(isA(MessageRequestDto.class));
        Error error = actualCreateAndPostMessageV2Result.getError();
        assertEquals("Invalid send time format", error.getError());
        assertNull(actualCreateAndPostMessageV2Result.getData());
        assertEquals(400, actualCreateAndPostMessageV2Result.getCode().intValue());
        assertEquals(400, error.getCode().intValue());
        assertFalse(actualCreateAndPostMessageV2Result.isSuccess());
    }

    /**
     * Test {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}.
     *
     * <ul>
     *   <li>Then return Error Error is {@code Scheduled time is in the past}.
     * </ul>
     *
     * <p>Method under test: {@link MessageServiceImpl#createAndPostMessageV2(MessageRequestDto)}
     */
    @Test
    @DisplayName(
            "Test createAndPostMessageV2(MessageRequestDto); then return Error Error is 'Scheduled time is in the past'")
    @Tag("ContributionFromDiffblue")
    @ManagedByDiffblue
    @MethodsUnderTest({"ResultData MessageServiceImpl.createAndPostMessageV2(MessageRequestDto)"})
    void testCreateAndPostMessageV2_thenReturnErrorErrorIsScheduledTimeIsInThePast()
            throws TelegramApiException {
        // Arrange
        LocalizedText description = new LocalizedText();
        description.setEn("En");
        description.setRu("Ru");
        description.setUz("Uz");

        LocalizedText title = new LocalizedText();
        title.setEn("En");
        title.setRu("Ru");
        title.setUz("Uz");

        Message message = new Message();
        message.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setDescription(description);
        message.setId(1L);
        message.setMessageId(1);
        message.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setTitle(title);
        when(messageRepository.save(Mockito.<Message>any())).thenReturn(message);

        LocalizedText description2 = new LocalizedText();
        description2.setEn("En");
        description2.setRu("Ru");
        description2.setUz("Uz");

        LocalizedText title2 = new LocalizedText();
        title2.setEn("En");
        title2.setRu("Ru");
        title2.setUz("Uz");

        Message message2 = new Message();
        message2.setCreateAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        message2.setDescription(description2);
        message2.setId(1L);
        message2.setMessageId(1);
        message2.setSendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        message2.setTitle(title2);
        when(messageMapper.toEntity(Mockito.<MessageRequestDto>any())).thenReturn(message2);

        MessageRequestDtoBuilder createdAtResult =
                MessageRequestDto.builder().createdAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        MessageRequestDtoBuilder sendTimeResult =
                createdAtResult
                        .description(LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build())
                        .messageId(1)
                        .sendPostDate(LocalDate.of(1970, 1, 1).atStartOfDay())
                        .sendTime("Send Time");

        // Act
        ResultData<MessageResponse> actualCreateAndPostMessageV2Result =
                messageServiceImpl.createAndPostMessageV2(
                        sendTimeResult
                                .title(LocalizedTextDto.builder().en("En").ru("Ru").uz("Uz").build())
                                .build());

        // Assert
        verify(messageRepository).save(isA(Message.class));
        verify(messageMapper).toEntity(isA(MessageRequestDto.class));
        Error error = actualCreateAndPostMessageV2Result.getError();
        assertEquals("Scheduled time is in the past", error.getError());
        assertNull(actualCreateAndPostMessageV2Result.getData());
        assertEquals(400, actualCreateAndPostMessageV2Result.getCode().intValue());
        assertEquals(400, error.getCode().intValue());
        assertFalse(actualCreateAndPostMessageV2Result.isSuccess());
    }
}
