package ru.skillbox.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class StartCommand implements Command {
    private final String startText = """
            Привет! Я Pomodoro-бот 🍅
        Помогу тебе работать продуктивно по методике Pomodoro.
        
        Команды:
        /start_pomo — запустить таймер
        /stop — остановить таймер
        /stats — статистика
        """;
    private final TelegramClient client;

    public StartCommand(TelegramClient client) {
        this.client = client;
    }

    @Override
    public void execute(Update update) {

        SendMessage sendMessage = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(startText)
                .build();

        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println("Could not send message in response of command /start");
            e.printStackTrace();
        }
    }
}
