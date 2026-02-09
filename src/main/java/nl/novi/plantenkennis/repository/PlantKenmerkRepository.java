package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.entity.PlantKenmerk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlantKenmerkRepository extends JpaRepository<PlantKenmerk, Long> {

    boolean existsByPlantSoortIdAndKenmerkId(Long plantSoortId, Long kenmerkId);

    Optional<PlantKenmerk> findByPlantSoortIdAndKenmerkId(Long plantSoortId, Long kenmerkId);

    List<PlantKenmerk> findByPlantSoortId(Long plantSoortId);

    void deleteByPlantSoortIdAndKenmerkId(Long plantSoortId, Long kenmerkId);
}
