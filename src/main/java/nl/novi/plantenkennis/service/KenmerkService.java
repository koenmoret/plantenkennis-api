package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Kenmerk;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.KenmerkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KenmerkService {

    private final KenmerkRepository repository;

    public KenmerkService(KenmerkRepository repository) {
        this.repository = repository;
    }

    public List<Kenmerk> getAll() {
        return repository.findAll();
    }

    public Kenmerk getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kenmerk niet gevonden met id: " + id));
    }

    public Kenmerk create(Kenmerk kenmerk) {
        boolean bestaatAl = repository.existsByTypeIgnoreCaseAndWaardeIgnoreCase(
                kenmerk.getType(),
                kenmerk.getWaarde()
        );

        if (bestaatAl) {
            throw new DuplicateResourceException(
                    "Kenmerk bestaat al: type='" + kenmerk.getType() + "', waarde='" + kenmerk.getWaarde() + "'"
            );
        }

        return repository.save(kenmerk);
    }
}
