package com.example.notification_system.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NotificationKafkaProperties.class)
public class KafkaConfig {
}
