package com.example.notification_system.banking.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.example.notification_system.event.NotificationSentEvent;
import com.example.notification_system.service.messaging.NotificationEventPublisher;

@Service
public class BankingNotificationPublisher {

    private final NotificationEventPublisher eventPublisher;

    public BankingNotificationPublisher(NotificationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publish(String eventType, String target, String message) {
        eventPublisher.publish(new NotificationSentEvent(eventType, target, message, Instant.now()));
    }
}
