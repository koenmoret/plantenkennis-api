package nl.novi.plantenkennis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "foto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // URL die je teruggeeft aan clients (bijv. /uploads/plantsoorten/12/<uuid>.jpg)
    @Column(nullable = false, length = 1000)
    private String url;

    // Relatief pad binnen upload root (bijv. plantsoorten/12/<uuid>.jpg)
    @Column(name = "storage_path", nullable = false, length = 500)
    private String storagePath;

    @Column(name = "original_filename", length = 255)
    private String originalFilename;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    private String fotograaf;
    private String licentie;

    @Column(name = "alt_tekst")
    private String altTekst;

    @Column(nullable = false)
    private boolean hoofdfoto;

    private String bron;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_soort_id", nullable = false)
    private PlantSoort plantSoort;
}
