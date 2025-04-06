package ru.dao;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransportationOrder {
    private final String id;
    private OrderStatus status;
    private final DeliveryRoute route;
    private final List<Message> messages = new ArrayList<>();
    private final Instant createdAt;

    public TransportationOrder(DeliveryRoute route) {
        this.id = UUID.randomUUID().toString();
        this.status = OrderStatus.ACTIVE;
        this.route = route;
        this.createdAt = Instant.now();
    }

    public void addMessage(Message message) {
        if (!status.isActive()) {
            throw new IllegalStateException("Нельзя добавить сообщение в неактивную заявку");
        }
        messages.add(message);
    }

    public void confirmMessage(String messageId, String userRole) {
        Message message = findMessageById(messageId);
        if (message == null) {
            throw new IllegalArgumentException("Сообщение не найдено");
        }
        if (message.getAuthorRole().equalsIgnoreCase(userRole)) {
            throw new IllegalStateException("Нельзя подтвердить своё собственное сообщение");
        }
        message.confirm();
    }

    public void closeIfInactive(Duration inactivityThreshold, Instant now) {
        if (!status.isActive()) return;

        boolean anyRecent = messages.stream()
                .anyMatch(msg -> msg.getCreatedAt().plus(inactivityThreshold).isAfter(now));

        if (!anyRecent) {
            this.status = OrderStatus.CLOSED;
        }
    }

    public void changeStatus(OrderStatus newStatus) {
        if (this.status.equals(OrderStatus.CLOSED)) {
            throw new IllegalStateException("Заявка уже закрыта");
        }
        this.status = newStatus;
    }

    private Message findMessageById(String id) {
        return messages.stream()
                .filter(msg -> msg.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public String getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public DeliveryRoute getRoute() {
        return route;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

