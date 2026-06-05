package com.example.payment.payment_service.controller;

import com.example.payment.payment_service.dto.TransferRequest;
import com.example.payment.payment_service.dto.TransferResponse;
import com.example.payment.payment_service.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public TransferResponse transfer(@RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }
}
