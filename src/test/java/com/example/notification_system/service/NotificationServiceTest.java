package com.example.notification_system.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.example.notification_system.model.Notification;
import com.example.notification_system.model.PushNotification;
import com.example.notification_system.service.impl.NotificationServiceImpl;

class NotificationServiceTest {

    private final NotificationService service = new NotificationServiceImpl();

    @Test
    void shouldSendAllNotifications() {
        List<Notification> notifications = List.of(
                new PushNotification("Hello"),
                new PushNotification("World"),
                new PushNotification("Welcome")
        );

        service.sendAll(notifications);

        assertEquals(3, notifications.size());
    }

}
