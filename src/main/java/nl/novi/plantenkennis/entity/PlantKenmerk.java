package nl.novi.plantenkennis.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "plantkenmerk",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_plantkenmerk_plant_kenmerk", columnNames = {"plant_soort_id", "kenmerk_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantKenmerk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Compositie: hoort bij precies 1 PlantSoort (FK verplicht)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plant_soort_id", nullable = false)
    private PlantSoort plantSoort;

    // Koppeling naar kenmerk (FK verplicht)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kenmerk_id", nullable = false)
    private Kenmerk kenmerk;
}
