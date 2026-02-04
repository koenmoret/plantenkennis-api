package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Kenmerk;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.KenmerkRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class KenmerkService {

    private final KenmerkRepository repository;

    public KenmerkService(KenmerkRepository repository) {
        this.repository = repository;
    }

    public List<Kenmerk> getAll() {
        // Optioneel: nette vaste sortering (makkelijker testen/UI)
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Kenmerk::getType, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Kenmerk::getWaarde, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public Kenmerk getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kenmerk niet gevonden met id: " + id));
    }

    public Kenmerk create(Kenmerk kenmerk) {
        // Optie A: type/waarde zijn niet blank door DTO-validatie, maar normalisatie blijft goed
        kenmerk.setType(kenmerk.getType().trim());
        kenmerk.setWaarde(kenmerk.getWaarde().trim());

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
