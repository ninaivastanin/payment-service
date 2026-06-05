package com.example.payment.payment_service.service;

import com.example.payment.payment_service.dto.TransferRequest;
import com.example.payment.payment_service.dto.TransferResponse;

/**
 * Service responsible for processing money transfers between accounts.
 */
public interface TransferService {

    /**
     * Transfers funds between two accounts.
     *
     * @param request transfer request
     * @return transfer response
     */
    TransferResponse transfer(TransferRequest request);
}
