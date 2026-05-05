package com.example.notification_system.dto;

import java.util.List;

public class NotificationResponse {

    private String status;
    private List<String> messages;

    public NotificationResponse(String status, List<String> messages) {
        this.status = status;
        this.messages = messages;
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public List<String> getMessages() {
        return messages;
    }
}
