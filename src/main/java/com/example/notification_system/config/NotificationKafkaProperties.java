package com.example.notification_system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification.kafka")
public record NotificationKafkaProperties(String topic) {
}
