package com.example.payment.payment_service.exception;

public class InvalidInitialBalanceException extends RuntimeException {
    public InvalidInitialBalanceException() {
        super("Initial balance cannot be negative");
    }
}
