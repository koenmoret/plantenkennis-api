package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.entity.Favoriet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavorietRepository extends JpaRepository<Favoriet, Long> {

    boolean existsByGebruikerIdAndPlantSoortId(Long gebruikerId, Long plantSoortId);

    List<Favoriet> findByGebruikerId(Long gebruikerId);
}
