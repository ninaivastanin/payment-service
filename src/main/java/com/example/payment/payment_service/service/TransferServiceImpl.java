package com.example.payment.payment_service.service;

import com.example.payment.payment_service.dto.TransferRequest;
import com.example.payment.payment_service.dto.TransferResponse;
import com.example.payment.payment_service.entity.Account;
import com.example.payment.payment_service.entity.Transfer;
import com.example.payment.payment_service.entity.TransferStatus;
import com.example.payment.payment_service.exception.AccountNotFoundException;
import com.example.payment.payment_service.exception.InsufficientFundsException;
import com.example.payment.payment_service.exception.InvalidTransferAccountsException;
import com.example.payment.payment_service.exception.InvalidTransferAmountException;
import com.example.payment.payment_service.repository.AccountRepository;
import com.example.payment.payment_service.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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
        validateDifferentAccounts(
                request.getSourceAccountId(),
                request.getDestinationAccountId()
        );

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
                "Transfer completed successfully",
                sourceAccount.getId(),
                destinationAccount.getId(),
                transfer.getAmount());
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
     * Validates that source and destination accounts are not the same.
     *
     * @param sourceId the ID of the source account
     * @param destinationId the ID of the destination account
     * @throws InvalidTransferAccountsException if both account IDs are equal
     */
    private void validateDifferentAccounts(Long sourceId, Long destinationId) {
        if (sourceId.equals(destinationId)) {
            throw new InvalidTransferAccountsException(sourceId);
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

    @Override
    @Transactional(readOnly = true)
    public List<TransferResponse> getAllTransfers() {

        return transferRepository.findAll()
                .stream()
                .map(this::mapTransfer)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransferResponse> getTransfersForAccount(Long accountId) {

        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        return transferRepository
                .findBySourceAccountIdOrDestinationAccountId(
                        accountId,
                        accountId
                )
                .stream()
                .map(this::mapTransfer)
                .toList();
    }

    private TransferResponse mapTransfer(Transfer transfer) {

        return new TransferResponse(
                transfer.getReference(),
                transfer.getStatus(),
                "Transfer found",
                transfer.getSourceAccount().getId(),
                transfer.getDestinationAccount().getId(),
                transfer.getAmount()
        );
    }
}
