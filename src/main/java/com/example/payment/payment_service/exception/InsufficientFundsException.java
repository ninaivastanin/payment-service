package com.example.payment.payment_service.exception;

import java.math.BigDecimal;

/**
 * Thrown when an account does not have sufficient funds for the transfer.
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Long accountId, BigDecimal balance, BigDecimal requestedAmount) {
        super(
                "Insufficient funds for account id " + accountId
                        + ". Current balance: " + balance
                        + ", requested amount: " + requestedAmount
        );
    }
}

