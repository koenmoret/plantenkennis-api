package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.entity.Favoriet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavorietRepository extends JpaRepository<Favoriet, Long> {

    boolean existsByGebruiker_IdAndPlantSoort_Id(Long gebruikerId, Long plantSoortId);

    @EntityGraph(attributePaths = {"gebruiker", "plantSoort"})
    List<Favoriet> findByGebruiker_Id(Long gebruikerId);

    @EntityGraph(attributePaths = {"gebruiker", "plantSoort"})
    Optional<Favoriet> findByGebruiker_IdAndPlantSoort_Id(Long gebruikerId, Long plantSoortId);
}
