package pl.akh.domainservicesvc.infrastructure.externalservices.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.infrastructure.pubsub.Publisher;
import pl.akh.notificationserviceapi.model.Notification;
import pl.akh.notificationserviceapi.services.NotificationService;

import java.util.UUID;

@Service(value = "AsyncNotificationServiceClient")
@Slf4j
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "async")
public class AsyncNotificationServiceClient implements NotificationService {

    private final Publisher<Notification> publisher;

    @Autowired
    public AsyncNotificationServiceClient(@Qualifier("notificationPublisher") Publisher<Notification> publisher) {
        this.publisher = publisher;
    }

    @Override
    public void sendNotification(Notification notification) throws Exception {
        log.info("Sending notification to topic.");
        publisher.publish(notification);
        log.info("Notification sent.");

    }
}
