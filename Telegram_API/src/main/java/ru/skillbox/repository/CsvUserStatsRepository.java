package ru.skillbox.repository;

import java.io.*;

public class CsvUserStatsRepository implements UserStatsRepository{

    private static final String DATA_DIR = "data/";

    public CsvUserStatsRepository() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
    @Override
    public void save(long userId, SessionRecord session) {
        String filePath = getFilePath(userId);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // write one CSV row
            writer.write(
                    session.date() + "," +
                            session.type() + "," +
                            session.durationMinutes()
            );
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Could not save session for user " + userId);
            e.printStackTrace();
        }
    }

    @Override
    public UserStats getStats(long userId) {
        String filePath = getFilePath(userId);

        int totalWorkMinutes = 0;
        int totalRestMinutes = 0;
        int workPeriods = 0;
        int restPeriods = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                SessionType type = SessionType.valueOf(parts[1]);
                int duration = Integer.parseInt(parts[2]);

                if (type == SessionType.WORK) {
                    totalWorkMinutes += duration;
                    workPeriods++;
                } else {
                    totalRestMinutes += duration;
                    restPeriods++;
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read stats for user " + userId);
            e.printStackTrace();
        }

        return new UserStats(totalWorkMinutes, totalRestMinutes, workPeriods, restPeriods);
    }

    private String getFilePath(long userId) {
        return DATA_DIR + "stats_" + userId + ".csv";
    }
}
