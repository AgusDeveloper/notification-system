package com.example.notification_system.model;

public class PushNotification extends Notification {

    public PushNotification(String message) {
        super(message);
    }

    @Override
    public String send() {
        return "Sending PUSH: " + getMessage();
    }

}
