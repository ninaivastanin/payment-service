package com.example.payment.payment_service.service;

import com.example.payment.payment_service.dto.AccountRequest;
import com.example.payment.payment_service.entity.Account;
import com.example.payment.payment_service.exception.AccountNotFoundException;
import com.example.payment.payment_service.exception.DuplicateAccountException;
import com.example.payment.payment_service.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void shouldCreateAccountSuccessfully() {

        AccountRequest request = new AccountRequest(
                "RS123",
                BigDecimal.valueOf(100),
                "EUR"
        );

        when(accountRepository.findByAccountNumber("RS123"))
                .thenReturn(Optional.empty());

        Account saved = new Account();
        saved.setId(1L);
        saved.setAccountNumber("RS123");
        saved.setBalance(BigDecimal.valueOf(100));
        saved.setCurrency("EUR");

        when(accountRepository.save(any(Account.class)))
                .thenReturn(saved);

        var result = accountService.createAccount(request);

        assertNotNull(result);
        assertEquals("RS123", result.getAccountNumber());
        assertEquals(BigDecimal.valueOf(100), result.getBalance());

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldThrowDuplicateAccountException() {

        AccountRequest request = new AccountRequest(
                "RS123",
                BigDecimal.valueOf(100),
                "EUR"
        );

        when(accountRepository.findByAccountNumber("RS123"))
                .thenReturn(Optional.of(new Account()));

        assertThrows(DuplicateAccountException.class,
                () -> accountService.createAccount(request));
    }

    @Test
    void shouldThrowAccountNotFoundException() {

        when(accountRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccount(1L));
    }
}
