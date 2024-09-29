package ru.mediasoft.shop.exception;

public class NotExpectedCustomerException extends RuntimeException {
    public NotExpectedCustomerException() {
        super("Not expected customer");
    }
}
