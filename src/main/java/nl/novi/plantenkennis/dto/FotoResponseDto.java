package nl.novi.plantenkennis.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class FotoResponseDto {
    private Long id;

    // Belangrijk voor clients:
    private String url;

    // Opslag info (handig voor debug / admin)
    private String storagePath;

    private String originalFilename;
    private String contentType;
    private Long fileSize;
    private Instant uploadedAt;

    private String fotograaf;
    private String licentie;
    private String altTekst;
    private Boolean hoofdfoto;
    private String bron;
}
