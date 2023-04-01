package pl.akh.domainservicesvc.infrastructure;

//import pl.akh.domainservicesvc.externalservices.notificationservice.NotificationServiceFactory;

import lombok.NonNull;
import pl.akh.domainservicesvc.infrastructure.storage.StorageFactory;

public class FactoryProvider {
    public static AbstractFactory getFactory(@NonNull Class factoryType) {
//        if (factoryType == NotificationServiceFactory.class) {
//            return new NotificationServiceFactory();
//        }
        if (factoryType == StorageFactory.class) {
            return new StorageFactory();
        }
        throw new UnsupportedOperationException();
    }
}
