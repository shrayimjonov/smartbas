package uz.davrbank.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.davrbank.config.bot.BotConfig;
import uz.davrbank.dto.request.MessageRequestDto;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class TelegramService extends TelegramLongPollingBot {
    private final BotConfig config;

    TelegramService(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        CompletableFuture.runAsync(() -> handleUpdate(update));
    }

    private void handleUpdate(Update update) {
        log.info("Has update message: {}", update.getMessage().getText());
        log.info("Chat ID: {}", update.getMessage().getChatId());
    }

    public Integer sendTextMessage(SendMessage sendMessage) throws TelegramApiException {
        return execute(sendMessage).getMessageId();
    }

    public void deleteMessageFromGroup(String chatId, Integer messageId) throws TelegramApiException {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        execute(deleteMessage);
    }

    public Integer executesPhoto(SendPhoto sendPhoto) throws TelegramApiException {
        return execute(sendPhoto).getMessageId();
    }

    public Integer sendPhoto(String chatId, InputFile photo, String caption, InlineKeyboardMarkup markup) throws TelegramApiException {
        return executesPhoto(SendPhoto.builder()
                .chatId(chatId)
                .photo(photo)
                .caption(caption)
                .replyMarkup(markup)
                .parseMode("HTML")
                .build());
    }

    public Integer sendMessage(String chatId, String text, InlineKeyboardMarkup markup) throws TelegramApiException {
        return sendTextMessage(SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(markup)
                .parseMode("HTML")
                .build());
    }

    protected InlineKeyboardMarkup buildApplyButton() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("откликнуться");
        button.setUrl("https://internal.example.com");

        return new InlineKeyboardMarkup(
                List.of(List.of(button))
        );
    }

    private String buildMessageText(MessageRequestDto req) {
        return Stream.of(req.getTitle().getText("uz"),
                        req.getTitle().getText("ru"),
                        req.getTitle().getText("en"),
                        req.getDescription().getText("uz"),
                        req.getDescription().getText("ru"),
                        req.getDescription().getText("en"))
                .filter(Objects::nonNull)
                .map(t -> t + "\n\n")
                .collect(Collectors.joining());
    }

    public Integer sendVacancyPostWithButton(String chatId, String vacancyDescription) throws TelegramApiException {
        SendMessage msg = new SendMessage(chatId, vacancyDescription);
        msg.setReplyMarkup(buildApplyButton());
        return sendTextMessage(msg);
    }

    public void updateMessageInGroup(String chatId, Integer messageId, MessageRequestDto requestDto) throws TelegramApiException {
        EditMessageText edit = new EditMessageText();
        edit.setChatId(chatId);
        edit.setMessageId(messageId);
        edit.setText(buildMessageText(requestDto));
        edit.setReplyMarkup(buildApplyButton());
        execute(edit);
    }
}