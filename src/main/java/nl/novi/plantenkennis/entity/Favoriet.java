package nl.novi.plantenkennis.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "favorieten",
        uniqueConstraints = @UniqueConstraint(columnNames = {"gebruikerId", "plantSoortId"})
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favoriet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long gebruikerId;

    @Column(nullable = false)
    private Long plantSoortId;
}
