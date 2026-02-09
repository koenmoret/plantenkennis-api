package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.entity.Kenmerk;
import nl.novi.plantenkennis.entity.PlantKenmerk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlantKenmerkRepository extends JpaRepository<PlantKenmerk, Long> {

    boolean existsByPlantSoortIdAndKenmerkId(Long plantSoortId, Long kenmerkId);

    Optional<PlantKenmerk> findByPlantSoortIdAndKenmerkId(Long plantSoortId, Long kenmerkId);

    List<PlantKenmerk> findByPlantSoortId(Long plantSoortId);

    void deleteByPlantSoortIdAndKenmerkId(Long plantSoortId, Long kenmerkId);

    // ✅ NIEUW: haal direct de kenmerken op (geen proxies)
    @Query("""
           select k
           from PlantKenmerk pk
           join pk.kenmerk k
           where pk.plantSoort.id = :plantSoortId
           """)
    List<Kenmerk> findKenmerkenByPlantSoortId(@Param("plantSoortId") Long plantSoortId);
}
