package ru.skillbox.timer;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.skillbox.repository.SessionRecord;
import ru.skillbox.repository.SessionType;
import ru.skillbox.repository.UserStatsRepository;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.*;

public class PomoTimerService {

    private static final int WORK_MINUTES = 25;
    private static final int REST_MINUTES = 5;

    private final TelegramClient client;
    private final UserStatsRepository repo;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final Map<Long, ScheduledFuture<?>> activeTimers = new ConcurrentHashMap<>();



    public PomoTimerService(TelegramClient client, UserStatsRepository repo) {
        this.client = client;
        this.repo = repo;
    }

    public void startTimer(long chatId) {
        stopTimer(chatId);
        ScheduledFuture<?> restFuture = scheduler.schedule(() -> {

            // ✅ work period just completed - save it now
            repo.save(chatId, new SessionRecord(
                    chatId,
                    LocalDate.now(),
                    SessionType.WORK,
                    WORK_MINUTES
            ));

            sendMessage(chatId, "Time to rest! 😴");

            ScheduledFuture<?> workFuture = scheduler.schedule(() -> {

                // ✅ rest period just completed - save it now
                repo.save(chatId, new SessionRecord(
                        chatId,
                        LocalDate.now(),
                        SessionType.REST,
                        REST_MINUTES
                ));

                sendPhoto(chatId, "Time to work! 💪");

            }, REST_MINUTES, TimeUnit.MINUTES);

            activeTimers.put(chatId, workFuture);

        }, WORK_MINUTES, TimeUnit.MINUTES);

        activeTimers.put(chatId, restFuture);
    }

    public void stopTimer(long chatId) {
        ScheduledFuture<?> future = activeTimers.get(chatId);
        if (future != null) {
            future.cancel(false);
            activeTimers.remove(chatId);
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(long chatId, String text) {
        try {
            InputStream imageStream = getClass().getResourceAsStream("/images/motivation.jpg");
            if (imageStream == null) {
                System.err.println("Image not found");
                sendMessage(chatId, text); // fallback to text only
                return;
            }

            InputFile inputFile = new InputFile(imageStream, "motivation.jpg");
            SendPhoto photo = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(inputFile)
                    .caption(text)
                    .build();

            client.execute(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
