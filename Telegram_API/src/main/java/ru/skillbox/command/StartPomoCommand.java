package ru.skillbox.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.skillbox.timer.PomoTimerService;

public class StartPomoCommand implements Command {
    private final TelegramClient client;
    private final PomoTimerService timerService;
    public StartPomoCommand(TelegramClient client, PomoTimerService timerService) {
        this.client = client;
        this.timerService = timerService;
    }

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();

        // 1. confirm to user that timer started
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Таймер запущен! Работаем 25 минут 🍅")
                .build();

        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Could not send start_pomo message");
            e.printStackTrace();
        }

        // 2. hand off to timer service
        timerService.startTimer(chatId);
    }
}
