package com.example.notification_system.banking.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.notification_system.banking.model.LoanApplication;
import com.example.notification_system.banking.model.LoanStatus;

public record LoanResponse(
        Long id,
        Long userId,
        String userEmail,
        BigDecimal amount,
        Integer termMonths,
        String purpose,
        LoanStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public static LoanResponse from(LoanApplication loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getUser().getId(),
                loan.getUser().getEmail(),
                loan.getAmount(),
                loan.getTermMonths(),
                loan.getPurpose(),
                loan.getStatus(),
                loan.getCreatedAt(),
                loan.getUpdatedAt()
        );
    }
}
