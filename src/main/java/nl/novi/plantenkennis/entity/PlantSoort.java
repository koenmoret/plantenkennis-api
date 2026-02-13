package nl.novi.plantenkennis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "plant_soorten")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantSoort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wetenschappelijke_naam")
    private String wetenschappelijkeNaam;

    @Column(name = "nederlandse_naam", nullable = false, unique = true)
    private String nederlandseNaam;

    private String familie;

    @Column(length = 2000)
    private String beschrijving;

    private Integer bloeiperiodeStart;

    private Integer bloeiperiodeEinde;

    @Column(nullable = false)
    private boolean giftig;

    @Column(nullable = false)
    private boolean inheems;

    @Column(length = 50)
    private String onderhoudsniveau;

    @Column(length = 120)
    private String slug;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "plantSoort", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();

    @OneToMany(mappedBy = "plantSoort", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlantKenmerk> plantKenmerken = new ArrayList<>();
}
