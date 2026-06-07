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
import java.util.List;
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

        verify(accountRepository).findByAccountNumber("RS123");
        verify(accountRepository).save(any(Account.class));
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

        verify(accountRepository).findByAccountNumber("RS123");
        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldThrowAccountNotFoundException() {

        when(accountRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccount(1L));

        verify(accountRepository).findById(1L);
    }

    @Test
    void shouldReturnAllAccounts() {

        Account acc1 = new Account();
        acc1.setId(1L);
        acc1.setAccountNumber("A1");
        acc1.setBalance(BigDecimal.valueOf(100));
        acc1.setCurrency("EUR");

        Account acc2 = new Account();
        acc2.setId(2L);
        acc2.setAccountNumber("A2");
        acc2.setBalance(BigDecimal.valueOf(200));
        acc2.setCurrency("EUR");

        when(accountRepository.findAll())
                .thenReturn(List.of(acc1, acc2));

        var result = accountService.getAllAccounts();

        assertEquals(2, result.size());
        assertEquals("A1", result.getFirst().getAccountNumber());

        verify(accountRepository).findAll();
    }
}
