package com.example.notification_system.model;

public class EmailNotification extends Notification {

    public EmailNotification(String message) {
        super(message);
    }

    @Override
    public String send() {
        return "Sending EMAIL: " + getMessage();
    }
}
