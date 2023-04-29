package pl.akh.domainservicesvc.infrastructure.storage.services;

import org.springframework.web.multipart.MultipartFile;
import pl.akh.domainservicesvc.infrastructure.storage.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalStorageService implements StorageService {
    private final Path prefixPath;

    public LocalStorageService(String prefixPath) {
        this.prefixPath = Path.of(prefixPath);
    }

    @Override
    public byte[] getFile(Path path) throws IOException {
        return Files.readAllBytes(Path.of(prefixPath.toString(), path.toString()));
    }

    @Override
    public void saveFile(Path path, MultipartFile file, String ext) throws IOException {
        Path of = Path.of(prefixPath.toString(), path.toString() + ext);
        Files.write(of, file.getBytes());
    }

    @Override
    public void deleteFile(Path path) throws IOException {
        Files.delete(Path.of(prefixPath.toString(), path.toString()));
    }
}