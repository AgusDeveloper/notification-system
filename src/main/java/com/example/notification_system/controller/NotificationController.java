package com.example.notification_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notification_system.dto.NotificationResponse;
import com.example.notification_system.model.EmailNotification;
import com.example.notification_system.model.Notification;
import com.example.notification_system.model.SmsNotification;
import com.example.notification_system.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/send")
    public NotificationResponse sendNotifications() {
        List<Notification> notifications = List.of(
                new EmailNotification("Hello via Email!"),
                new SmsNotification("Hello via SMS!")
        );
        List<String> messages = notificationService.sendAll(notifications);
        return new NotificationResponse("success", messages);
    }

}
