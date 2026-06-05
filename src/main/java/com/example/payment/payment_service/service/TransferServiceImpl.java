package com.example.payment.payment_service.service;

import com.example.payment.payment_service.dto.TransferRequest;
import com.example.payment.payment_service.dto.TransferResponse;
import com.example.payment.payment_service.entity.Account;
import com.example.payment.payment_service.entity.Transfer;
import com.example.payment.payment_service.entity.TransferStatus;
import com.example.payment.payment_service.exception.AccountNotFoundException;
import com.example.payment.payment_service.exception.InsufficientFundsException;
import com.example.payment.payment_service.exception.InvalidTransferAmountException;
import com.example.payment.payment_service.repository.AccountRepository;
import com.example.payment.payment_service.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Default implementation of money transfer processing.
 */
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    /**
     * Transfers funds between two accounts.
     */
    @Override
    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        validateAmount(request.getAmount());

        Account sourceAccount =
                accountRepository.findById(request.getSourceAccountId())
                        .orElseThrow(() ->
                                new AccountNotFoundException(
                                        request.getSourceAccountId()));

        Account destinationAccount =
                accountRepository.findById(request.getDestinationAccountId())
                        .orElseThrow(() ->
                                new AccountNotFoundException(
                                        request.getDestinationAccountId()));

        validateSufficientFunds(sourceAccount, request.getAmount());

        sourceAccount.setBalance(
                sourceAccount.getBalance()
                        .subtract(request.getAmount()));

        destinationAccount.setBalance(
                destinationAccount.getBalance()
                        .add(request.getAmount()));

        Transfer transfer = new Transfer();

        transfer.setSourceAccount(sourceAccount);
        transfer.setDestinationAccount(destinationAccount);
        transfer.setAmount(request.getAmount());
        transfer.setStatus(TransferStatus.SUCCESS);
        transfer.setReference(UUID.randomUUID());

        transferRepository.save(transfer);

        return new TransferResponse(
                transfer.getReference(),
                transfer.getStatus(),
                "Transfer completed successfully");
    }

    /**
     * Validates transfer amount.
     */
    private void validateAmount(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferAmountException(amount);
        }
    }

    /**
     * Validates available account balance.
     */
    private void validateSufficientFunds(Account account, BigDecimal amount) {

        if (account.getBalance().compareTo(amount) < 0) {

            throw new InsufficientFundsException(
                    account.getId(),
                    account.getBalance(),
                    amount);
        }
    }
}
