package ru;

import ru.dao.*;
import ru.rep.DriverRepository;
import ru.rep.TransportationOrderRepository;
import ru.service.DriverScheduleService;
import ru.service.FraudDetectionService;
import ru.service.OrderLifecycleService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TransportationOrderRepository repo = new TransportationOrderRepository();
        OrderLifecycleService lifecycle = new OrderLifecycleService();
        FraudDetectionService fraud = new FraudDetectionService();

        System.out.println("Создание заявки...");
        DeliveryRoute route = new DeliveryRoute(Arrays.asList("Москва", "Казань", "Екатеринбург"));
        TransportationOrder order = new TransportationOrder(route);
        repo.save(order);

        System.out.println("\nДобавление сообщения...");
        try {
            Message msg = new Message("CLIENT", "Когда будет доставка?", null);
            order.addMessage(msg);
            System.out.println("Сообщение успешно добавлено");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nПодтверждение сообщения другой стороной...");
        try {
            Message latest = order.getMessages().get(order.getMessages().size() - 1);
            order.confirmMessage(latest.getId(), "OPERATOR");
            System.out.println("Сообщение подтверждено успешно");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nПопытка подтвердить своё сообщение...");
        try {
            Message selfMsg = new Message("OPERATOR", "Ожидайте", null);
            order.addMessage(selfMsg);
            order.confirmMessage(selfMsg.getId(), "OPERATOR");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nЗакрытие заявки вручную...");
        try {
            lifecycle.closeOrder(order);
            System.out.println("Заявка вручную закрыта");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nДобавление сообщения в закрытую заявку...");
        try {
            order.addMessage(new Message("CLIENT", "Есть кто живой?", null));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nПопытка сменить статус у закрытой заявки...");
        try {
            order.changeStatus(OrderStatus.ACTIVE);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\nСоздание новой заявки для авто-закрытия...");
        TransportationOrder inactiveOrder = new TransportationOrder(route);
        repo.save(inactiveOrder);
        lifecycle.autoCloseIfInactive(inactiveOrder, Duration.ofSeconds(1), Instant.now().plusSeconds(2));
        System.out.println("Статус заявки после проверки неактивности: " + inactiveOrder.getStatus().getValue());

        System.out.println("\nСоздание новой заявки для подозрительности...");
        TransportationOrder suspiciousOrder = new TransportationOrder(route);
        repo.save(suspiciousOrder);

        for (int i = 0; i < 5; i++) {
            try {
                suspiciousOrder.addMessage(new Message("CLIENT", "Спам #" + i, null));
            } catch (Exception e) {
                System.out.println("Ошибка при добавлении сообщения: " + e.getMessage());
            }
        }

        boolean suspicious = fraud.isSuspicious(suspiciousOrder, 4, 10);
        System.out.println("Подозрительная активность обнаружена? " + suspicious);

        System.out.println("\nУдаление заявки...");
        repo.delete(order.getId());

        System.out.println("\nПоиск заявки по ID...");
        TransportationOrder found = repo.findById(inactiveOrder.getId());
        if (found != null) {
            System.out.println("Найдена заявка со статусом: " + found.getStatus().getValue());
        } else {
            System.out.println("Заявка не найдена");
        }

        System.out.println("\n--- Работа с графиком водителей ---");

        DriverRepository driverRepo = new DriverRepository();
        DriverScheduleService scheduleService = new DriverScheduleService();

        Driver driver = new Driver("Сергей Бебебаба");
        driverRepo.save(driver);
        System.out.println("Водитель сохранен: " + driver.getFullName());
        System.out.println("ID: " + driver.getId());
        System.out.println("Статус: " + (driver.isActive() ? "АКТИВЕН" : "НЕАКТИВЕН"));

        try {
            scheduleService.addShift(driver, new Shift(
                    LocalDateTime.of(2025, 4, 5, 8, 0),
                    LocalDateTime.of(2025, 4, 5, 12, 0)
            ));
            scheduleService.addShift(driver, new Shift(
                    LocalDateTime.of(2025, 4, 5, 13, 0),
                    LocalDateTime.of(2025, 4, 5, 18, 0)
            ));
            scheduleService.addShift(driver, new Shift(
                    LocalDateTime.of(2025, 4, 5, 10, 0),
                    LocalDateTime.of(2025, 4, 5, 14, 0)
            ));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при добавлении смены: " + e.getMessage());
        }

        System.out.println("Доступность в 2025-04-05T10:00: " +
                scheduleService.isDriverAvailableAt(driver, LocalDateTime.of(2025, 4, 5, 10, 0)));
        System.out.println("Доступность в 2025-04-05T12:30: " +
                scheduleService.isDriverAvailableAt(driver, LocalDateTime.of(2025, 4, 5, 12, 30)));
        System.out.println("Доступность в 2025-04-05T18:30: " +
                scheduleService.isDriverAvailableAt(driver, LocalDateTime.of(2025, 4, 5, 18, 30)));

        System.out.println("Смены на 5 апреля:");
        List<Shift> shifts = scheduleService.getDriverShiftsForDate(driver, LocalDate.of(2025, 4, 5));
        for (Shift shift : shifts) {
            System.out.println(shift.getStart() + " - " + shift.getEnd());
        }

        System.out.println("\n=== Конец демонстрации ===");
    }
}
