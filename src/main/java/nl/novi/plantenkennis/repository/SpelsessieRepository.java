package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.model.Spelsessie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpelsessieRepository extends JpaRepository<Spelsessie, Long> {
    List<Spelsessie> findTop10ByGebruikerIdOrderByGespeeldOpDesc(Long gebruikerId);
}
