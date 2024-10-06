package com.springBoot.fruits_ecommerce.exception;

public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException(String message) {
        super(message);
    }
}