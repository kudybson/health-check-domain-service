package pl.akh.domainservicesvc.infrastructure.storage;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import pl.akh.domainservicesvc.infrastructure.AbstractFactory;
import pl.akh.domainservicesvc.infrastructure.storage.services.CloudStorageService;
import pl.akh.domainservicesvc.infrastructure.storage.services.InMemoryStorageService;
import pl.akh.domainservicesvc.infrastructure.storage.services.LocalStorageService;

@Slf4j
public class StorageFactory implements AbstractFactory<StorageService> {
    @Override
    public StorageService create(@NonNull String type) {
        log.info("Creating StorageService with bean class: {}", type);
        if ("inMemory".equals(type)) {
            log.warn("Creating in memory storage is not recommended on production, " +
                    "it may produce errors with pods by running out off memory.");
        }
        return switch (type) {
            case "cloud" -> new CloudStorageService();
            case "local" -> new LocalStorageService();
            default -> new InMemoryStorageService();
        };
    }
}