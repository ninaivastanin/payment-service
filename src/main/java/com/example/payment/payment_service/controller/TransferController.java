package com.example.payment.payment_service.controller;

import com.example.payment.payment_service.dto.TransferRequest;
import com.example.payment.payment_service.dto.TransferResponse;
import com.example.payment.payment_service.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for money transfers.
 */
@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    /**
     * Processes a money transfer request.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponse transfer(@RequestBody @Valid TransferRequest request) {
        return transferService.transfer(request);
    }

    @GetMapping
    public List<TransferResponse> getAllTransfers() {
        return transferService.getAllTransfers();
    }

    @GetMapping("/account/{accountId}")
    public List<TransferResponse> getTransfersForAccount(
            @PathVariable Long accountId) {

        return transferService.getTransfersForAccount(accountId);
    }
}
