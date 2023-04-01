package pl.akh.domainservicesvc.infrastructure.storage.services;

import org.springframework.web.multipart.MultipartFile;
import pl.akh.domainservicesvc.infrastructure.storage.StorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class CloudStorageService implements StorageService {
    @Override
    public byte[] getFile(Path path) throws IOException {
        throw new RuntimeException();
    }

    @Override
    public void saveFile(Path path, MultipartFile file) throws IOException {
        throw new RuntimeException();
    }

    @Override
    public void deleteFile(Path path) throws IOException {
        throw new RuntimeException();
    }
}
