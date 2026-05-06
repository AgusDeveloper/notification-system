package com.example.notification_system.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.notification_system.event.NotificationSentEvent;
import com.example.notification_system.model.Notification;
import com.example.notification_system.model.PushNotification;
import com.example.notification_system.service.impl.NotificationServiceImpl;
import com.example.notification_system.service.messaging.NotificationEventPublisher;

class NotificationServiceTest {

    private final NotificationEventPublisher eventPublisher = mock(NotificationEventPublisher.class);
    private final NotificationService service = new NotificationServiceImpl(eventPublisher);

    @Test
    void shouldSendAllNotifications() {
        List<Notification> notifications = List.of(
                new PushNotification("Hello"),
                new PushNotification("World"),
                new PushNotification("Welcome")
        );

        List<String> messages = service.sendAll(notifications);

        assertEquals(3, notifications.size());
        assertEquals(List.of("Sending PUSH: Hello", "Sending PUSH: World", "Sending PUSH: Welcome"), messages);
        verify(eventPublisher, times(3)).publish(any(NotificationSentEvent.class));
    }

}
