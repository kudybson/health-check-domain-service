package pl.akh.domainservicesvc.externalservices.notificationservice;

import lombok.extern.slf4j.Slf4j;
import pl.akh.domainservicesvc.externalservices.AbstractFactory;
import pl.akh.domainservicesvc.externalservices.notificationservice.clients.AsyncNotificationServiceClient;
import pl.akh.domainservicesvc.externalservices.notificationservice.clients.MockNotificationServiceClient;
import pl.akh.domainservicesvc.externalservices.notificationservice.clients.SyncNotificationServiceClient;
import pl.akh.notificationserviceapi.services.NotificationService;

import java.util.InputMismatchException;

@Slf4j
public class NotificationServiceFactory implements AbstractFactory<NotificationService> {

    @Override
    public NotificationService create(String notificationClientType) {
        log.info("Creating NotificationService with bean class: {}", notificationClientType);
        return switch (notificationClientType) {
            case "mock" -> new MockNotificationServiceClient();
            case "async" -> new AsyncNotificationServiceClient();
            case "sync" -> new SyncNotificationServiceClient();
            default -> throw new InputMismatchException();
        };
    }
}
