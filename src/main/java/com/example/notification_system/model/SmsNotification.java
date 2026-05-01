package com.example.notification_system.model;

public class SmsNotification extends Notification {

    public SmsNotification(String message) {
        super(message);
    }

    @Override
    public String send() {
        return "Sending SMS: " + getMessage();
    }
}
