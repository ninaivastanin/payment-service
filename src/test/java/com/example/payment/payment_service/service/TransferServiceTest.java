package com.example.payment.payment_service.service;

import com.example.payment.payment_service.dto.TransferRequest;
import com.example.payment.payment_service.entity.Account;
import com.example.payment.payment_service.entity.Transfer;
import com.example.payment.payment_service.entity.TransferStatus;
import com.example.payment.payment_service.exception.AccountNotFoundException;
import com.example.payment.payment_service.exception.InsufficientFundsException;
import com.example.payment.payment_service.exception.InvalidTransferAccountsException;
import com.example.payment.payment_service.repository.AccountRepository;
import com.example.payment.payment_service.repository.TransferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        verify(accountRepository, times(2)).findById(anyLong());
        verify(transferRepository).save(any());
    }

    @Test
    void shouldThrowWhenTransferToSameAccount() {

        TransferRequest request = new TransferRequest(
                1L,
                1L,
                BigDecimal.valueOf(100)
        );

        assertThrows(InvalidTransferAccountsException.class,
                () -> transferService.transfer(request));
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

        verify(transferRepository, never()).save(any());
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

    @Test
    void shouldReturnAllTransfers() {

        Account source = new Account();
        source.setId(1L);

        Account destination = new Account();
        destination.setId(2L);

        Transfer t1 = new Transfer();
        t1.setReference(UUID.randomUUID());
        t1.setSourceAccount(source);
        t1.setDestinationAccount(destination);
        t1.setAmount(BigDecimal.valueOf(100));
        t1.setStatus(TransferStatus.SUCCESS);

        when(transferRepository.findAll())
                .thenReturn(List.of(t1));

        var result = transferService.getAllTransfers();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getSourceAccountId());

        verify(transferRepository).findAll();
    }

    @Test
    void shouldReturnTransfersForAccount() {

        Long accountId = 1L;

        Account account = new Account();
        account.setId(accountId);

        Transfer t1 = new Transfer();
        t1.setReference(UUID.randomUUID());
        t1.setSourceAccount(account);
        t1.setDestinationAccount(new Account());
        t1.setAmount(BigDecimal.valueOf(100));
        t1.setStatus(TransferStatus.SUCCESS);

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        when(transferRepository.findBySourceAccountIdOrDestinationAccountId(accountId, accountId))
                .thenReturn(List.of(t1));

        var result = transferService.getTransfersForAccount(accountId);

        assertEquals(1, result.size());

        verify(accountRepository).findById(accountId);
        verify(transferRepository)
                .findBySourceAccountIdOrDestinationAccountId(accountId, accountId);
    }

    @Test
    void shouldThrowWhenAccountNotFoundForTransfers() {

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> transferService.getTransfersForAccount(1L));

        verify(accountRepository).findById(1L);
        verifyNoInteractions(transferRepository);
    }
}
