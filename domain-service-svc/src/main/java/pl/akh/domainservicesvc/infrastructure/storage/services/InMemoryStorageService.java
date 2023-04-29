package pl.akh.domainservicesvc.infrastructure.storage.services;

import org.springframework.web.multipart.MultipartFile;
import pl.akh.domainservicesvc.infrastructure.storage.StorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class InMemoryStorageService implements StorageService {
    private final Map<String, byte[]> files = new HashMap<>();

    @Override
    public byte[] getFile(Path path) throws IOException {
        return files.get(path.toString());
    }

    @Override
    public void saveFile(Path path, MultipartFile file, String ext) throws IOException {
        files.put(path.toString(), file.getBytes());
    }

    @Override
    public void deleteFile(Path path) throws IOException {
        files.remove(path.toString());
    }
}
