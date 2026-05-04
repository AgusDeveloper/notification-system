package com.example.notification_system.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.notification_system.service.NotificationService;

public class NotificationControllerTest {

    private final NotificationService notificationService = mock(NotificationService.class);
    private final NotificationController controller = new NotificationController(notificationService);

    @Test
    void sendNotificationsShouldReturnSuccessMessage() {
        String result = controller.sendNotifications();

        assertEquals("Notifications sent!", result);
        verify(notificationService).sendAll(anyList());
    }

}
