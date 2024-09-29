package ru.mediasoft.shop.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(UUID id) {
        super(String.format("Order with id [%s] is not found", id));
    }
}
