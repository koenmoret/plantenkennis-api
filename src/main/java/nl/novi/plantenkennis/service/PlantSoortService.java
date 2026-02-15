package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlantSoortService {

    private final PlantSoortRepository repository;

    public PlantSoortService(PlantSoortRepository repository) {
        this.repository = repository;
    }

    // ===== bestaande basisfunctionaliteit =====

    public List<PlantSoort> getAll() {
        return repository.findAll();
    }

    public PlantSoort getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlantSoort niet gevonden met id: " + id));
    }

    public PlantSoort create(PlantSoort entity) {
        // voorbeeld simpele validatie (optioneel)
        if (entity.getNederlandseNaam() == null || entity.getNederlandseNaam().trim().isEmpty()) {
            throw new IllegalArgumentException("Nederlandse naam is verplicht.");
        }
        if (repository.existsByNederlandseNaamIgnoreCase(entity.getNederlandseNaam())) {
            throw new IllegalArgumentException("PlantSoort bestaat al met Nederlandse naam: " + entity.getNederlandseNaam());
        }
        return repository.save(entity);
    }

    // ===== zoeken/filteren op plant_soorten kolommen =====

    public Page<PlantSoort> searchTableFields(
            String q,
            String familie,
            Boolean giftig,
            Boolean inheems,
            String onderhoudsniveau,
            String slug,
            Integer bloeimaand,
            String updatedAfter,
            String updatedBefore,
            Pageable pageable
    ) {
        LocalDateTime after = parseDateTime(updatedAfter);
        LocalDateTime before = parseDateTime(updatedBefore);

        return repository.searchTableFields(
                normalize(q),
                normalize(familie),
                giftig,
                inheems,
                normalizeUpper(onderhoudsniveau),
                normalize(slug),
                bloeimaand,
                after,
                before,
                pageable
        );
    }

    // ===== helpers =====

    private String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private String normalizeUpper(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t.toUpperCase();
    }

    private LocalDateTime parseDateTime(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return LocalDateTime.parse(s.trim()); // ISO-8601
    }
}
