package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.model.Gebruiker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GebruikerRepository extends JpaRepository<Gebruiker, Long> {
    Optional<Gebruiker> findByEmail(String email);
}
