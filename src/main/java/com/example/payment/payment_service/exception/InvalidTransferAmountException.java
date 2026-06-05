package com.example.payment.payment_service.exception;

import java.math.BigDecimal;

/**
 * Thrown when an invalid transfer amount is provided.
 */
public class InvalidTransferAmountException extends RuntimeException {

    public InvalidTransferAmountException(BigDecimal amount) {
        super("Invalid transfer amount: " + amount);
    }
}

