package com.example.payment.payment_service.dto;

import com.example.payment.payment_service.entity.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO representing the response returned after transfer processing.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

    private UUID reference;

    private TransferStatus status;

    private String message;

    private Long sourceAccountId;

    private Long destinationAccountId;

    private BigDecimal amount;
}
