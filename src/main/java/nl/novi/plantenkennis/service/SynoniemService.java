package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.entity.Synoniem;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import nl.novi.plantenkennis.repository.SynoniemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SynoniemService {

    private final SynoniemRepository synoniemRepository;
    private final PlantSoortRepository plantSoortRepository;

    public SynoniemService(SynoniemRepository synoniemRepository, PlantSoortRepository plantSoortRepository) {
        this.synoniemRepository = synoniemRepository;
        this.plantSoortRepository = plantSoortRepository;
    }

    public List<Synoniem> getByPlantSoort(Long plantSoortId) {
        if (!plantSoortRepository.existsById(plantSoortId)) {
            throw new ResourceNotFoundException("PlantSoort niet gevonden: " + plantSoortId);
        }
        return synoniemRepository.findByPlantSoortId(plantSoortId);
    }

    public Synoniem addToPlant(Long plantSoortId, String naam) {
        if (!plantSoortRepository.existsById(plantSoortId)) {
            throw new ResourceNotFoundException("PlantSoort niet gevonden: " + plantSoortId);
        }

        String clean = naam.trim();
        if (synoniemRepository.existsByPlantSoortIdAndNaamIgnoreCase(plantSoortId, clean)) {
            throw new DuplicateResourceException("Synoniem bestaat al voor deze plant: " + clean);
        }

        PlantSoort plantRef = plantSoortRepository.getReferenceById(plantSoortId);

        Synoniem s = Synoniem.builder()
                .naam(clean)
                .plantSoort(plantRef)
                .build();

        return synoniemRepository.save(s);
    }

    public void removeFromPlant(Long plantSoortId, Long synoniemId) {
        Synoniem s = synoniemRepository.findById(synoniemId)
                .orElseThrow(() -> new ResourceNotFoundException("Synoniem niet gevonden: " + synoniemId));

        if (!s.getPlantSoort().getId().equals(plantSoortId)) {
            throw new ResourceNotFoundException("Synoniem hoort niet bij PlantSoort: " + plantSoortId);
        }

        synoniemRepository.delete(s);
    }
}
