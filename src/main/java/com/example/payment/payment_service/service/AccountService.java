package com.example.payment.payment_service.service;

import com.example.payment.payment_service.dto.AccountRequest;
import com.example.payment.payment_service.dto.AccountResponse;

/**
 * Service responsible for account management.
 */
public interface AccountService {

    AccountResponse createAccount(AccountRequest request);

    AccountResponse getAccount(Long id);
}
