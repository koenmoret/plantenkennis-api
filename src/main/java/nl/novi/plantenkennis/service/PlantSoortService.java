package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.springframework.stereotype.Service;

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

    public PlantSoort create(PlantSoort plantSoort) {
        // minimale check, later vervangen door nette exception + DTO
        if (repository.existsByNaamIgnoreCase(plantSoort.getNaam())) {
            throw new IllegalArgumentException("PlantSoort bestaat al: " + plantSoort.getNaam());
        }
        return repository.save(plantSoort);
    }
}
