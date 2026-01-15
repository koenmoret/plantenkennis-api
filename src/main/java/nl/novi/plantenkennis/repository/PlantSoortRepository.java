package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.entity.PlantSoort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlantSoortRepository extends JpaRepository<PlantSoort, Long> {
    boolean existsByNederlandseNaamIgnoreCase(String nederlandseNaam);
}
