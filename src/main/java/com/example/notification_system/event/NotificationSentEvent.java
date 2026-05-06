package com.example.notification_system.event;

import java.time.Instant;

public record NotificationSentEvent(
        String notificationType,
        String originalMessage,
        String sentMessage,
        Instant sentAt
) {
}
