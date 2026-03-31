package ru.skillbox;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.skillbox.command.CommandDispatcher;
import ru.skillbox.config.Config;
import ru.skillbox.config.ConfigReader;
import ru.skillbox.config.ConfigReaderEnvironment;
import ru.skillbox.repository.CsvUserStatsRepository;
import ru.skillbox.repository.UserStatsRepository;
import ru.skillbox.timer.PomoTimerService;


public class Main {
    public static void main(String[] args) throws Exception {
        ConfigReader configReader = new ConfigReaderEnvironment();
        Config config = configReader.read();

        TelegramClient telegramClient = new OkHttpTelegramClient(config.botApiToken());
        UserStatsRepository repository = new CsvUserStatsRepository();
        PomoTimerService timerService = new PomoTimerService(telegramClient, repository);
        CommandDispatcher dispatcher = new CommandDispatcher(telegramClient, timerService, repository);

        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
        botsApplication.registerBot(config.botApiToken(), new PomoBot(telegramClient, dispatcher));

        System.out.println("Bot started successfully!");
    }
}