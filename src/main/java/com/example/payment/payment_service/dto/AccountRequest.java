package com.example.payment.payment_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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

    @NotBlank(message = "Account number must not be blank")
    private String accountNumber;

    @Positive(message = "Initial balance must be positive or zero")
    private BigDecimal initialBalance;

    @NotBlank(message = "Currency must not be blank")
    private String currency;
}
