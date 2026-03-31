package ru.skillbox.repository;

import java.time.LocalDate;

public record SessionRecord(
        long userId,
        LocalDate date,
        SessionType type,
        int durationMinutes) {


}
