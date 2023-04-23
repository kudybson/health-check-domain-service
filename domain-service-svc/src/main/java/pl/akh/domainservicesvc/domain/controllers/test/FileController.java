package pl.akh.domainservicesvc.domain.controllers.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.akh.domainservicesvc.infrastructure.storage.StorageService;

import java.io.IOException;
import java.nio.file.Path;

@RestController
public class FileController {

    private final StorageService storageService;

    private final Path sample = Path.of("D:", "volumes", "health-check");

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/file", produces = "application/octet-stream")
    public ResponseEntity<byte[]> getFile() {
        try {
            return ResponseEntity.ok(storageService.getFile(sample));
        } catch (IOException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/file")
    public ResponseEntity<String> saveFile(@RequestParam("file") MultipartFile file) {
        try {
            storageService.saveFile(sample, file);
            return ResponseEntity.ok("OK");
        } catch (IOException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
