package ru.service;

import ru.dao.Driver;
import ru.dao.Shift;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DriverScheduleService {

    public void addShift(Driver driver, Shift shift) {
        driver.addShift(shift);
    }

    public boolean isDriverAvailableAt(Driver driver, LocalDateTime time) {
        return driver.isAvailableAt(time);
    }

    public List<Shift> getDriverShiftsForDate(Driver driver, LocalDate date) {
        return driver.getShiftsForDate(date);
    }
}
