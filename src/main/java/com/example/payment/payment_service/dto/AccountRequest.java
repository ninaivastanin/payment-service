package com.example.payment.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO representing request for creating a new account.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    private String accountNumber;

    private BigDecimal initialBalance;

    private String currency;
}
