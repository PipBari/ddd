package ru.dao;

public class OrderStatus {
    public static final OrderStatus ACTIVE = new OrderStatus("ACTIVE");
    public static final OrderStatus CLOSED = new OrderStatus("CLOSED");
    public static final OrderStatus CANCELLED = new OrderStatus("CANCELLED");

    private final String value;

    private OrderStatus(String value) {
        this.value = value;
    }

    public boolean isActive() {
        return this.equals(ACTIVE);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderStatus)) return false;
        OrderStatus that = (OrderStatus) o;
        return value.equalsIgnoreCase(that.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase().hashCode();
    }
}

