package com.example.notification_system.service;

import java.util.List;

import com.example.notification_system.model.Notification;

public interface NotificationService {

    List<String> sendAll(List<Notification> notifications);
}
