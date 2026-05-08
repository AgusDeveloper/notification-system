package com.example.notification_system.banking.dto;

import java.time.Instant;

import com.example.notification_system.banking.model.BankUser;
import com.example.notification_system.banking.model.UserStatus;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        String phoneNumber,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public static UserResponse from(BankUser user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
