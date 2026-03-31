package ru.skillbox.repository;

public record UserStats(      int totalWorkMinutes,
                              int totalRestMinutes,
                              int workPeriods,
                              int restPeriods) {
}
