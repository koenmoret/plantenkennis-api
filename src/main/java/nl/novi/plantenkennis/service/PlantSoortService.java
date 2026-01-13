package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.springframework.stereotype.Service;

import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class PlantSoortService {

    private final PlantSoortRepository repository;

    public PlantSoortService(PlantSoortRepository repository) {
        this.repository = repository;
    }

    public List<PlantSoort> getAll() {
        return repository.findAll();
    }

    public PlantSoort getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlantSoort niet gevonden: " + id));
    }

    public PlantSoort create(PlantSoort plantSoort) {
        if (repository.existsByNaamIgnoreCase(plantSoort.getNaam())) {
            throw new DuplicateResourceException("PlantSoort bestaat al: " + plantSoort.getNaam());
        }
        return repository.save(plantSoort);
    }
}
