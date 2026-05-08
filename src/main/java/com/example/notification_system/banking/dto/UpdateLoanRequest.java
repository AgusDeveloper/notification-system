package com.example.notification_system.banking.dto;

import java.math.BigDecimal;

import com.example.notification_system.banking.model.LoanStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateLoanRequest(
        @NotNull @DecimalMin("1.00") BigDecimal amount,
        @NotNull @Min(1) Integer termMonths,
        @NotBlank String purpose,
        @NotNull LoanStatus status
) {
}
