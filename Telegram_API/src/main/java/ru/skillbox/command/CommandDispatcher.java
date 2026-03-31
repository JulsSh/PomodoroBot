package ru.skillbox.command;
import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.skillbox.repository.UserStatsRepository;
import ru.skillbox.timer.PomoTimerService;

public class CommandDispatcher {
    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandDispatcher(TelegramClient telegramClient, PomoTimerService timerService, UserStatsRepository repository){
        commandMap.put("/start", new StartCommand(telegramClient));
        commandMap.put("/start_pomo", new StartPomoCommand(telegramClient, timerService));
        commandMap.put("/stop", new StopPomoCommand(telegramClient, timerService));
        commandMap.put("/stats", new StatsCommand(telegramClient, repository));
    }

    public void dispatch(String commandText, Update update) {
        String commandKey = commandText.split("\\s+")[0].toLowerCase();
        Command command = commandMap.get(commandKey);

        System.out.printf("Processing command: %s%n", commandText);

        if (command != null) {
            command.execute(update);
        } else {
            System.out.printf("Command not found: %s%n", commandText);
        }
    }
}
