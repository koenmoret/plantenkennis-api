package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FotoRepository extends JpaRepository<Foto, Long> {

    List<Foto> findByPlantSoortId(Long plantSoortId);

    Optional<Foto> findFirstByPlantSoortIdAndHoofdfotoTrue(Long plantSoortId);

    Optional<Foto> findByIdAndPlantSoortId(Long id, Long plantSoortId);
}
