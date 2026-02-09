package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Kenmerk;
import nl.novi.plantenkennis.entity.PlantKenmerk;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.KenmerkRepository;
import nl.novi.plantenkennis.repository.PlantKenmerkRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class KenmerkService {

    private final KenmerkRepository repository;
    private final PlantKenmerkRepository plantKenmerkRepository;

    public KenmerkService(KenmerkRepository repository, PlantKenmerkRepository plantKenmerkRepository) {
        this.repository = repository;
        this.plantKenmerkRepository = plantKenmerkRepository;
    }

    public List<Kenmerk> getAll() {
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
