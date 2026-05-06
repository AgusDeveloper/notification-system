package com.example.notification_system.service.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.notification_system.config.NotificationKafkaProperties;
import com.example.notification_system.event.NotificationSentEvent;

@Service
public class KafkaNotificationEventPublisher implements NotificationEventPublisher {

    private final KafkaTemplate<String, NotificationSentEvent> kafkaTemplate;
    private final NotificationKafkaProperties kafkaProperties;

    public KafkaNotificationEventPublisher(
            KafkaTemplate<String, NotificationSentEvent> kafkaTemplate,
            NotificationKafkaProperties kafkaProperties
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProperties = kafkaProperties;
    }

    @Override
    public void publish(NotificationSentEvent event) {
        kafkaTemplate.send(kafkaProperties.topic(), event.notificationType(), event);
    }
}
