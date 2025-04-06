package ru.rep;

import ru.dao.TransportationOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportationOrderRepository {
    private final Map<String, TransportationOrder> storage = new HashMap<>();

    public void save(TransportationOrder order) {
        storage.put(order.getId(), order);
        System.out.println("Заявка сохранена: ID = " + order.getId());
    }

    public TransportationOrder findById(String id) {
        return storage.get(id);
    }

    public List<TransportationOrder> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void delete(String id) {
        storage.remove(id);
        System.out.println("Заявка удалена: ID = " + id);
    }

    public void clear() {
        storage.clear();
    }
}

