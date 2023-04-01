package pl.akh.domainservicesvc.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {
    byte[] getFile(Path path) throws IOException;

    void saveFile(Path path, MultipartFile file) throws IOException;

    void deleteFile(Path path) throws IOException;

}