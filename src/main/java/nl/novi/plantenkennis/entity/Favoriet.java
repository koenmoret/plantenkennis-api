package nl.novi.plantenkennis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "favorieten",
        uniqueConstraints = @UniqueConstraint(columnNames = {"gebruiker_id", "plant_soort_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favoriet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Optioneel, maar handig (staat in schema.sql)
    @Column(name = "aangemaakt_op", nullable = false)
    private LocalDateTime aangemaaktOp;

    @PrePersist
    protected void onCreate() {
        if (aangemaaktOp == null) {
            aangemaaktOp = LocalDateTime.now();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gebruiker_id", nullable = false)
    @JsonIgnore
    private Gebruiker gebruiker;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plant_soort_id", nullable = false)
    @JsonIgnore
    private PlantSoort plantSoort;
}
