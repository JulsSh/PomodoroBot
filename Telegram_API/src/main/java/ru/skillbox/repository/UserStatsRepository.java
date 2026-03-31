package ru.skillbox.repository;

public interface UserStatsRepository {
    void save(long userId, SessionRecord session);
    UserStats getStats(long userId);
}
