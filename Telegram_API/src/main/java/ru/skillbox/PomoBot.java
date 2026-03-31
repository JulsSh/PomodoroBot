package ru.skillbox;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.skillbox.command.CommandDispatcher;

public class PomoBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final CommandDispatcher commandDispatcher;

    public PomoBot(TelegramClient telegramClient, CommandDispatcher commandDispatcher) {
        this.telegramClient = telegramClient;
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            System.out.println("Message has no text, skipping update " + update.getUpdateId());
            return;
        }

        String text = update.getMessage().getText();

        if (text.startsWith("/")) {
            commandDispatcher.dispatch(text, update);
        }
    }
}