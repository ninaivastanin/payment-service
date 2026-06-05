package com.example.payment.payment_service.service;

import com.example.payment.payment_service.dto.AccountRequest;
import com.example.payment.payment_service.dto.AccountResponse;
import com.example.payment.payment_service.entity.Account;
import com.example.payment.payment_service.exception.AccountNotFoundException;
import com.example.payment.payment_service.exception.DuplicateAccountException;
import com.example.payment.payment_service.exception.InvalidInitialBalanceException;
import com.example.payment.payment_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Implementation of AccountService.
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest request) {

        accountRepository.findByAccountNumber(request.getAccountNumber())
                .ifPresent(acc -> {
                    throw new DuplicateAccountException(request.getAccountNumber());
                });

        if (request.getInitialBalance() != null &&
                request.getInitialBalance().compareTo(BigDecimal.ZERO) < 0) {

            throw new InvalidInitialBalanceException();
        }

        Account account = new Account();

        account.setAccountNumber(request.getAccountNumber());
        account.setBalance(
                request.getInitialBalance() != null
                        ? request.getInitialBalance()
                        : BigDecimal.ZERO
        );
        account.setCurrency(request.getCurrency());

        Account saved = accountRepository.save(account);

        return new AccountResponse(
                saved.getId(),
                saved.getAccountNumber(),
                saved.getBalance(),
                saved.getCurrency()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency()
        );
    }
}
