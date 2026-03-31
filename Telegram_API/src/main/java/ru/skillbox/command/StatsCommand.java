package ru.skillbox.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.skillbox.repository.UserStats;
import ru.skillbox.repository.UserStatsRepository;

public class StatsCommand implements Command {
    private final  TelegramClient client;
    private final  UserStatsRepository repository;

    public StatsCommand(TelegramClient client, UserStatsRepository repository) {
        this.client = client;
        this.repository = repository;
    }

    @Override
    public void execute(Update update) {
    long chatId = update.getMessage().getChatId();
        long userId = update.getMessage().getFrom().getId();

        UserStats stats = repository.getStats(userId);

        String text = "📊 Your stats:\n\n" +
                "🍅 Work periods: " + stats.workPeriods() + "\n" +
                "😴 Rest periods: " + stats.restPeriods() + "\n" +
                "⏱ Total work time: " + stats.totalWorkMinutes() + " min\n" +
                "☕ Total rest time: " + stats.totalRestMinutes() + " min";

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Could not send stats message");
            e.printStackTrace();
        }
    }
}
