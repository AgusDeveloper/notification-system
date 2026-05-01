package com.example.notification_system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.notification_system.model.Notification;

@Service
public class NotificationService {

    public void sendAll(List<Notification> notifications) {
        for (Notification n : notifications) {
            System.out.println(n.send());
        }
    }
}
