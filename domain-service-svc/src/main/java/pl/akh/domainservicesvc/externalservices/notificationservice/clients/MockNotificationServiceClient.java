package pl.akh.domainservicesvc.externalservices.notificationservice.clients;

import lombok.extern.slf4j.Slf4j;
import pl.akh.notificationserviceapi.model.Notification;
import pl.akh.notificationserviceapi.services.NotificationService;

@Slf4j
public class MockNotificationServiceClient implements NotificationService {
    @Override
    public void sendNotification(Notification notification) throws Exception {
        log.debug("Notification for user {} has been sent.", notification.getUserId());
    }
}
