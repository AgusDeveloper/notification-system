package com.example.notification_system.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.notification_system.event.NotificationSentEvent;
import com.example.notification_system.model.Notification;
import com.example.notification_system.service.NotificationService;
import com.example.notification_system.service.messaging.NotificationEventPublisher;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationEventPublisher eventPublisher;

    public NotificationServiceImpl(NotificationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<String> sendAll(List<Notification> notifications) {
        return notifications.stream()
                .map(this::sendAndPublish)
                .toList();
    }

    private String sendAndPublish(Notification notification) {
        String sentMessage = notification.send();
        NotificationSentEvent event = new NotificationSentEvent(
                notification.getClass().getSimpleName(),
                notification.getMessage(),
                sentMessage,
                Instant.now()
        );
        eventPublisher.publish(event);
        return sentMessage;
    }
}
