package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.entity.Synoniem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SynoniemRepository extends JpaRepository<Synoniem, Long> {

    List<Synoniem> findByPlantSoortId(Long plantSoortId);

    boolean existsByPlantSoortIdAndNaamIgnoreCase(Long plantSoortId, String naam);
}
