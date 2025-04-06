package ru.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Shift {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public Shift(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Окончание смены раньше начала.");
        }
        this.start = start;
        this.end = end;
    }

    public boolean overlapsWith(Shift other) {
        return !(end.isBefore(other.start) || start.isAfter(other.end));
    }

    public boolean contains(LocalDateTime time) {
        return !time.isBefore(start) && !time.isAfter(end);
    }

    public boolean isOnDate(LocalDate date) {
        return start.toLocalDate().equals(date) || end.toLocalDate().equals(date);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
