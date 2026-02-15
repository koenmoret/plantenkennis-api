package nl.novi.plantenkennis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class FotoStorageService {

    private final Path uploadRoot;

    // Voor de URL die je opslaat in de DB (bijv. "/uploads")
    private final String publicBasePath;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    public FotoStorageService(
            @Value("${app.upload.root:uploads}") String root,
            @Value("${app.upload.public-base:/uploads}") String publicBasePath
    ) {
        this.uploadRoot = Paths.get(root).toAbsolutePath().normalize();
        this.publicBasePath = (publicBasePath == null || publicBasePath.isBlank()) ? "/uploads" : publicBasePath;
    }

    public StoredFile store(Long plantSoortId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Geen bestand meegegeven.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Alleen afbeeldingsbestanden (jpeg/png/webp/gif) zijn toegestaan.");
        }

        // uploads/plantsoorten/{plantSoortId}/
        Path targetDir = uploadRoot
                .resolve("plantsoorten")
                .resolve(String.valueOf(plantSoortId));

        try {
            Files.createDirectories(targetDir);
        } catch (IOException e) {
            throw new RuntimeException("Kon uploadmap niet aanmaken: " + targetDir, e);
        }

        String extension = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + extension;
        Path targetFile = targetDir.resolve(filename);

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Opslaan mislukt: " + targetFile, e);
        }

        // Relatief t.o.v. uploadRoot (voor DB)
        String storagePath = uploadRoot.relativize(targetFile)
                .toString()
                .replace("\\", "/"); // Windows-fix

        // URL voor client (ook voor DB)
        String url = publicBasePath + "/" + storagePath;

        return new StoredFile(
                storagePath,
                file.getOriginalFilename(),
                contentType,
                file.getSize(),
                url
        );
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int i = filename.lastIndexOf('.');
        if (i < 0) return "";
        return filename.substring(i);
    }

    public record StoredFile(
            String storagePath,
            String originalFilename,
            String contentType,
            long fileSize,
            String url
    ) {}

    public void delete(String storagePath) {

        try {
            Path file = uploadRoot.resolve(storagePath).normalize();

            if (Files.exists(file)) {
                Files.delete(file);
            }

        } catch (IOException e) {
            throw new RuntimeException("Verwijderen van bestand mislukt: " + storagePath, e);
        }
    }

}
