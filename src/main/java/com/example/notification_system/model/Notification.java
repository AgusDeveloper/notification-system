package com.example.notification_system.model;

public abstract class Notification {

    private final String message;

    public Notification(String message) {
        this.message = message;
    }

    public final String getMessage() {
        return message;
    }

    public abstract String send();

}
