package pl.akh.domainservicesvc.infrastructure.externalservices.notificationservice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.akh.notificationserviceapi.model.Notification;
import pl.akh.notificationserviceapi.services.NotificationService;

@Service
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "sync")
public class SyncNotificationServiceClient implements NotificationService {
    @Override
    public void sendNotification(Notification notification) throws Exception {
        //TODO
        throw new RuntimeException();
    }
}
