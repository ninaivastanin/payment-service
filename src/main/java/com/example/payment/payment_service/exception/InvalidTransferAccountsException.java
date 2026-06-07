package com.example.payment.payment_service.exception;

public class InvalidTransferAccountsException extends RuntimeException {

    public InvalidTransferAccountsException(Long accountId) {
        super("Source and destination accounts cannot be the same: " + accountId);
    }
}
