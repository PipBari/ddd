package ru.dao;

import java.util.Collections;
import java.util.List;

public class DeliveryRoute {
    private final List<String> checkpoints;

    public DeliveryRoute(List<String> checkpoints) {
        if (checkpoints == null || checkpoints.isEmpty()) {
            throw new IllegalArgumentException("Маршрут не может быть пустым");
        }
        this.checkpoints = List.copyOf(checkpoints);
    }

    public List<String> getCheckpoints() {
        return Collections.unmodifiableList(checkpoints);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryRoute)) return false;
        DeliveryRoute that = (DeliveryRoute) o;
        return checkpoints.equals(that.checkpoints);
    }

    @Override
    public int hashCode() {
        return checkpoints.hashCode();
    }
}
