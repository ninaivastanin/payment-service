package com.example.payment.payment_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Standard error response returned by API.
 */
@Getter
@AllArgsConstructor
public class ApiError {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;
}
