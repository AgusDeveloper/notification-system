package com.example.notification_system.service.messaging;

import com.example.notification_system.event.NotificationSentEvent;

public interface NotificationEventPublisher {

    void publish(NotificationSentEvent event);
}
