package pl.akh.domainservicesvc.infrastructure.externalservices.notificationservice.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.akh.notificationserviceapi.model.Notification;
import pl.akh.notificationserviceapi.services.NotificationService;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "mock")
public class MockNotificationServiceClient implements NotificationService {
    @Override
    public void sendNotification(Notification notification) throws Exception {
        log.info("Notification for user {} has been sent.", notification.getUserId());
    }
}
