package pl.akh.domainservicesvc.infrastructure.storage.services;

import org.springframework.web.multipart.MultipartFile;
import pl.akh.domainservicesvc.infrastructure.storage.StorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.Key;

public class CloudStorageService implements StorageService {

    private String prefixPath;
    private Key key;

    public CloudStorageService(String prefixPath, Key key) {
        this.key = key;
        this.prefixPath = prefixPath;
    }

    @Override
    public byte[] getFile(Path path) throws IOException {
        throw new RuntimeException();
    }

    @Override
    public void saveFile(Path path, MultipartFile file, String ext) throws IOException {
        throw new RuntimeException();
    }

    @Override
    public void deleteFile(Path path) throws IOException {
        throw new RuntimeException();
    }
}
