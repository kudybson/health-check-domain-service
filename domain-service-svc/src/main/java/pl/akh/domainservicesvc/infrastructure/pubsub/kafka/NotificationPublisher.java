package pl.akh.domainservicesvc.infrastructure.pubsub.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.infrastructure.pubsub.Publisher;
import pl.akh.notificationserviceapi.model.Notification;

import java.util.UUID;

@Service(value = "notificationPublisher")
public class NotificationPublisher implements Publisher<Notification> {
    private KafkaTemplate<String, Notification> kafkaTemplate;
    private final String topic = "notifications";

    @Autowired
    public NotificationPublisher(KafkaTemplate<String, Notification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void publish(Notification message) {
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), message);
    }
}
