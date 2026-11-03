package uz.davrbank.config.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.davrbank.service.impl.TelegramService;


@Component
@Slf4j
@RequiredArgsConstructor
public class BotInitializer {

    private final TelegramService controller;

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            log.info("Bot starts");
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(controller);
        } catch (TelegramApiException e) {
            log.error("Error occurred during bot initialization: {}", e.getMessage());
        }
    }
}

