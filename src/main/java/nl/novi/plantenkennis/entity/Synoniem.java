package nl.novi.plantenkennis.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "synoniem",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_synoniem_plantsyn",
            columnNames = {"plant_soort_id", "naam"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Synoniem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String naam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plant_soort_id", nullable = false)
    private PlantSoort plantSoort;
}
