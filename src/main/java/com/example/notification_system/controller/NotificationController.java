package com.example.notification_system.controller;

import com.example.notification_system.model.*;
import com.example.notification_system.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/send")
    public String sendNotifications() {
        List<Notification> notifications = List.of(
                new EmailNotification("Hello via Email!"),
                new SmsNotification("Hello via SMS!")
        );
        notificationService.sendAll(notifications);
        return "Notifications sent!";
    }

}
