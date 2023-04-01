package pl.akh.domainservicesvc.infrastructure.storage.services;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.akh.domainservicesvc.infrastructure.storage.StorageService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Files;

public class LocalStorageService implements StorageService {
    @Override
    public byte[] getFile(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    @Override
    public void saveFile(Path path, MultipartFile file) throws IOException {
        Files.write(path, file.getBytes());
    }

    @Override
    public void deleteFile(Path path) throws IOException {
        Files.delete(path);
    }
}