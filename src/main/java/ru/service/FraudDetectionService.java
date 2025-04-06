package ru.service;

import ru.dao.Message;
import ru.dao.TransportationOrder;

import java.time.Instant;
import java.util.List;

public class FraudDetectionService {

    public boolean isSuspicious(TransportationOrder order, int messageThreshold, int secondsWindow) {
        List<Message> messages = order.getMessages();
        if (messages.size() < messageThreshold) return false;

        Instant windowStart = Instant.now().minusSeconds(secondsWindow);
        int count = 0;

        for (Message msg : messages) {
            if (msg.getCreatedAt().isAfter(windowStart)) {
                count++;
                if (count >= messageThreshold) {
                    return true;
                }
            }
        }

        return false;
    }
}

