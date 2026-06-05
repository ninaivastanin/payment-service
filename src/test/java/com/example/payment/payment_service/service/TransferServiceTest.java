package com.example.payment.payment_service.service;

import com.example.payment.payment_service.dto.TransferRequest;
import com.example.payment.payment_service.entity.Account;
import com.example.payment.payment_service.exception.AccountNotFoundException;
import com.example.payment.payment_service.exception.InsufficientFundsException;
import com.example.payment.payment_service.repository.AccountRepository;
import com.example.payment.payment_service.repository.TransferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private TransferServiceImpl transferService;

    @Test
    void shouldTransferSuccessfully() {

        TransferRequest request = new TransferRequest(
                1L,
                2L,
                BigDecimal.valueOf(50)
        );

        Account source = new Account();
        source.setId(1L);
        source.setBalance(BigDecimal.valueOf(100));

        Account destination = new Account();
        destination.setId(2L);
        destination.setBalance(BigDecimal.valueOf(0));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));

        when(transferRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        var result = transferService.transfer(request);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50), source.getBalance());
        assertEquals(BigDecimal.valueOf(50), destination.getBalance());

        verify(transferRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowInsufficientFundsException() {

        TransferRequest request = new TransferRequest(
                1L,
                2L,
                BigDecimal.valueOf(200)
        );

        Account source = new Account();
        source.setId(1L);
        source.setBalance(BigDecimal.valueOf(100));

        Account destination = new Account();
        destination.setId(2L);
        destination.setBalance(BigDecimal.ZERO);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destination));

        assertThrows(InsufficientFundsException.class,
                () -> transferService.transfer(request));
    }

    @Test
    void shouldThrowAccountNotFoundException() {

        TransferRequest request = new TransferRequest(
                1L,
                2L,
                BigDecimal.valueOf(50)
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> transferService.transfer(request));
    }
}
