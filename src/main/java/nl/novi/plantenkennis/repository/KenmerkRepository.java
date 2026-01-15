package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.entity.Kenmerk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KenmerkRepository extends JpaRepository<Kenmerk, Long> {

    boolean existsByTypeIgnoreCaseAndWaardeIgnoreCase(String type, String waarde);
}
