package ru.mediasoft.shop.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(UUID id){
        super(String.format("Product with id [%s] is not found", id));
    }
}
