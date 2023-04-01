package pl.akh.domainservicesvc.externalservices;

import pl.akh.domainservicesvc.externalservices.notificationservice.NotificationServiceFactory;

public class FactoryProvider {
    public static AbstractFactory getFactory(Class factoryType) {
//        if (factoryType == NotificationServiceFactory.class) {
//            return new NotificationServiceFactory();
//        }

        throw new UnsupportedOperationException();
    }
}
