package nl.novi.plantenkennis.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, length = 1000)
    private String url;

    private String fotograaf;
    private String licentie;

    @Column(name = "alt_tekst")
    private String altTekst;

    private Boolean hoofdfoto;

    private String bron;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_soort_id", nullable = false)
    private PlantSoort plantSoort;
}
