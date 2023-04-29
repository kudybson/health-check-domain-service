package pl.akh.domainservicesvc.infrastructure.storage;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import pl.akh.domainservicesvc.infrastructure.AbstractFactory;
import pl.akh.domainservicesvc.infrastructure.storage.services.CloudStorageService;
import pl.akh.domainservicesvc.infrastructure.storage.services.InMemoryStorageService;
import pl.akh.domainservicesvc.infrastructure.storage.services.LocalStorageService;

import java.security.Key;
import java.util.Arrays;

@Slf4j
public class StorageFactory implements AbstractFactory<StorageService> {
    @Override
    public StorageService create(@NonNull String type, Pair<String, String>... metaParams) {
        log.info("Creating StorageService with bean class: {}", type);
        log.info("Creating storage with metaParams: {}", metaParams);
        if ("inMemory".equals(type)) {
            log.warn("Creating in memory storage is not recommended on production, " +
                    "it may produce errors with pods by running out off memory.");
        }
        return switch (type) {
            case "cloud" ->
                    new CloudStorageService(getStoragePrefixPath(metaParams), getStorageEncryptionKey(metaParams));
            case "local" -> new LocalStorageService(getStoragePrefixPath(metaParams));
            default -> new InMemoryStorageService();
        };
    }

    private String getStoragePrefixPath(Pair<String, String>[] metaParams) {
        return Arrays.stream(metaParams)
                .filter(pair -> "storage.path".equals(pair.getFirst()))
                .findFirst()
                .map(Pair::getSecond)
                .orElseThrow();
    }

    private Key getStorageEncryptionKey(Pair<String, String>[] metaParams) {
        String path = Arrays.stream(metaParams)
                .filter(pair -> "storage.encryption-key".equals(pair.getFirst()))
                .findFirst()
                .map(Pair::getSecond)
                .orElseThrow();
        //create encryption key for cloud storage
        throw new RuntimeException();
    }
}