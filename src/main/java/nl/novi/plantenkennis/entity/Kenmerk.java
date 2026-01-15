package nl.novi.plantenkennis.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kenmerken")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kenmerk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String waarde;
}
