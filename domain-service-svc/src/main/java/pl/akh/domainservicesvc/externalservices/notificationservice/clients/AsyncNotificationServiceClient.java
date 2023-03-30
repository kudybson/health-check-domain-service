package pl.akh.domainservicesvc.externalservices.notificationservice.clients;

import pl.akh.notificationserviceapi.model.Notification;
import pl.akh.notificationserviceapi.services.NotificationService;

public class AsyncNotificationServiceClient implements NotificationService {
    @Override
    public void sendNotification(Notification notification) throws Exception {
        //TODO
        throw new RuntimeException();
    }
}
