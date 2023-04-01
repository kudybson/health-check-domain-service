package pl.akh.domainservicesvc.infrastructure.externalservices.notificationservice.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.akh.notificationserviceapi.model.Notification;
import pl.akh.notificationserviceapi.services.NotificationService;

import java.util.UUID;

@Service(value = "AsyncNotificationServiceClient")
@Slf4j
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "async")
public class AsyncNotificationServiceClient implements NotificationService {

    private KafkaTemplate<String, Notification> kafkaTemplate;

    @Autowired
    public AsyncNotificationServiceClient(KafkaTemplate<String, Notification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendNotification(Notification notification) throws Exception {
        log.info("Sending notification to topic.");
        kafkaTemplate.send("notification", UUID.randomUUID().toString(), notification);
        log.info("Notification sent.");

    }
}
