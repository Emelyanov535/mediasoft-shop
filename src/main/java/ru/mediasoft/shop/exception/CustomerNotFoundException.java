package ru.mediasoft.shop.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super(String.format("Customer with id [%s] is not found", id));
    }
}
