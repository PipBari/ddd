package ru.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Driver {
    private final String id;
    private final String fullName;
    private boolean active;
    private final List<Shift> shifts = new ArrayList<>();

    public Driver(String fullName) {
        this.id = UUID.randomUUID().toString();
        this.fullName = fullName;
        this.active = true;
    }

    public void addShift(Shift shift) {
        for (Shift s : shifts) {
            if (s.overlapsWith(shift)) {
                throw new IllegalArgumentException("Смена пересекается с существующей.");
            }
        }
        shifts.add(shift);
    }

    public List<Shift> getShiftsForDate(LocalDate date) {
        return shifts.stream()
                .filter(s -> s.isOnDate(date))
                .collect(Collectors.toList());
    }

    public boolean isAvailableAt(LocalDateTime dateTime) {
        return shifts.stream().noneMatch(s -> s.contains(dateTime));
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
