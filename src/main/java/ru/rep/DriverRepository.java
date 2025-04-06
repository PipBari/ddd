package ru.rep;

import ru.dao.Driver;

import java.util.*;

public class DriverRepository {
    private final Map<String, Driver> storage = new HashMap<>();

    public void save(Driver driver) {
        storage.put(driver.getId(), driver);
        System.out.println("Водитель сохранен: ID = " + driver.getId());
    }

    public Driver findById(String id) {
        return storage.get(id);
    }

    public List<Driver> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void delete(String id) {
        storage.remove(id);
        System.out.println("Водитель удален: ID = " + id);
    }

    public void clear() {
        storage.clear();
    }
}
