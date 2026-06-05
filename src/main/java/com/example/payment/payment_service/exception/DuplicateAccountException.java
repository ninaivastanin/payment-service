package com.example.payment.payment_service.exception;

/**
 * Thrown when account with given account number already exists.
 */
public class DuplicateAccountException extends RuntimeException {

    public DuplicateAccountException(String accountNumber) {
        super("Account already exists with accountNumber: " + accountNumber);
    }
}
