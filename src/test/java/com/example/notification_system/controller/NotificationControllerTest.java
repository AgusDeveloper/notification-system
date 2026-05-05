package com.example.notification_system.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.notification_system.dto.NotificationResponse;
import com.example.notification_system.service.NotificationService;

public class NotificationControllerTest {

    private final NotificationService notificationService = mock(NotificationService.class);
    private final NotificationController controller = new NotificationController(notificationService);

    @Test
    void sendNotificationsShouldReturnSuccessMessage() {
        List<String> messages = List.of("Email sent: Hello via Email!", "SMS sent: Hello via SMS!");
        when(notificationService.sendAll(anyList())).thenReturn(messages);

        NotificationResponse result = controller.sendNotifications();

        assertEquals("success", result.getStatus());
        assertEquals(messages, result.getMessages());
        verify(notificationService).sendAll(anyList());
    }

}
