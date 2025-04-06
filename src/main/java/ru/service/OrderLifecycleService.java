package ru.service;

import ru.dao.OrderStatus;
import ru.dao.TransportationOrder;

import java.time.Duration;
import java.time.Instant;

public class OrderLifecycleService {

    public void cancelOrder(TransportationOrder order) {
        order.changeStatus(OrderStatus.CANCELLED);
    }

    public void closeOrder(TransportationOrder order) {
        order.changeStatus(OrderStatus.CLOSED);
    }

    public void autoCloseIfInactive(TransportationOrder order, Duration inactivityThreshold, Instant now) {
        order.closeIfInactive(inactivityThreshold, now);
    }

    public boolean isClosed(TransportationOrder order) {
        return order.getStatus().equals(OrderStatus.CLOSED);
    }
}

