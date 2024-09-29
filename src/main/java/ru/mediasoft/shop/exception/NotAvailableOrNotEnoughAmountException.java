package ru.mediasoft.shop.exception;

import java.util.UUID;

public class NotAvailableOrNotEnoughAmountException extends RuntimeException {
    public NotAvailableOrNotEnoughAmountException(UUID uuid) {
        super(String.format("Product with article [%s] is not or is not enough amount", uuid));
    }
}
