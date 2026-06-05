package com.example.payment.payment_service.exception;

/**
 * Thrown when the requested account does not exist in the database.
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long accountId) {
        super("Account not found with id: " + accountId);
    }
}

