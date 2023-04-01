package pl.akh.domainservicesvc.config.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.akh.domainservicesvc.infrastructure.AbstractFactory;
import pl.akh.domainservicesvc.infrastructure.FactoryProvider;
import pl.akh.domainservicesvc.infrastructure.storage.StorageFactory;
import pl.akh.domainservicesvc.infrastructure.storage.StorageService;

@Configuration
public class StorageProvider {
    @Value("${storage.type}")
    private String type;

    @Bean
    public StorageService getStorageService() {
        StorageFactory factory = (StorageFactory) FactoryProvider.getFactory(StorageFactory.class);
        return factory.create(type);
    }
}
