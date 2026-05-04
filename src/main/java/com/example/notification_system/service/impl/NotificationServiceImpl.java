package com.example.notification_system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.notification_system.model.Notification;
import com.example.notification_system.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public List<String> sendAll(List<Notification> notifications) {
        return notifications.stream()
                .map(Notification::send)
                .collect(Collectors.toList());
    }
}
