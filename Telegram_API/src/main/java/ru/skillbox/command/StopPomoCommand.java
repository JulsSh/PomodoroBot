package ru.skillbox.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.skillbox.timer.PomoTimerService;

public class StopPomoCommand implements Command {
    private final TelegramClient client;
    private final PomoTimerService timerService;

    public StopPomoCommand(TelegramClient client, PomoTimerService timerService) {
        this.client = client;
        this.timerService = timerService;
    }

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Timer stopped!  🍅")
                .build();

        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Could not send stop_pomo message");
            e.printStackTrace();
        }

        timerService.stopTimer(chatId);
    }
}
