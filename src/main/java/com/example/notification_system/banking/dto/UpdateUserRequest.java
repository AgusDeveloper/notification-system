package com.example.notification_system.banking.dto;

import com.example.notification_system.banking.model.UserStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotBlank String phoneNumber,
        @NotNull UserStatus status
) {
}
