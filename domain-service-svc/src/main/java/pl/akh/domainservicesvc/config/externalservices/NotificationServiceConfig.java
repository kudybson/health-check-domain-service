package pl.akh.domainservicesvc.config.externalservices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.akh.domainservicesvc.externalservices.AbstractFactory;
import pl.akh.domainservicesvc.externalservices.FactoryProvider;
import pl.akh.domainservicesvc.externalservices.notificationservice.NotificationServiceFactory;
import pl.akh.notificationserviceapi.services.NotificationService;

@Configuration
public class NotificationServiceConfig {
    @Value("${notificationservice}")
    private String notificationServiceType;

    @Bean
    public NotificationService getNotificationService() {
        NotificationServiceFactory factory = (NotificationServiceFactory) FactoryProvider.getFactory(NotificationServiceFactory.class);
        return factory.create(notificationServiceType);
    }
}
