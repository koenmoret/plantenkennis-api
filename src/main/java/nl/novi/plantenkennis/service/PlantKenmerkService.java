package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Kenmerk;
import nl.novi.plantenkennis.entity.PlantKenmerk;
import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.KenmerkRepository;
import nl.novi.plantenkennis.repository.PlantKenmerkRepository;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantKenmerkService {

    private final PlantKenmerkRepository plantKenmerkRepository;
    private final PlantSoortRepository plantSoortRepository;
    private final KenmerkRepository kenmerkRepository;

    public PlantKenmerkService(PlantKenmerkRepository plantKenmerkRepository,
                               PlantSoortRepository plantSoortRepository,
                               KenmerkRepository kenmerkRepository) {
        this.plantKenmerkRepository = plantKenmerkRepository;
        this.plantSoortRepository = plantSoortRepository;
        this.kenmerkRepository = kenmerkRepository;
    }

    public List<PlantKenmerk> getByPlantSoort(Long plantSoortId) {
        // Validatie: plant moet bestaan (anders stille lege lijst is verwarrend)
        if (!plantSoortRepository.existsById(plantSoortId)) {
            throw new ResourceNotFoundException("PlantSoort niet gevonden: " + plantSoortId);
        }
        return plantKenmerkRepository.findByPlantSoortId(plantSoortId);
    }

    public List<Kenmerk> getKenmerkenVoorPlant(Long plantSoortId) {
        if (!plantSoortRepository.existsById(plantSoortId)) {
            throw new ResourceNotFoundException("PlantSoort niet gevonden: " + plantSoortId);
        }
        return plantKenmerkRepository.findKenmerkenByPlantSoortId(plantSoortId);
    }

    public PlantKenmerk addKenmerkToPlant(Long plantSoortId, Long kenmerkId) {
        PlantSoort plant = plantSoortRepository.findById(plantSoortId)
                .orElseThrow(() -> new ResourceNotFoundException("PlantSoort niet gevonden: " + plantSoortId));

        Kenmerk kenmerk = kenmerkRepository.findById(kenmerkId)
                .orElseThrow(() -> new ResourceNotFoundException("Kenmerk niet gevonden: " + kenmerkId));

        boolean bestaatAl = plantKenmerkRepository.existsByPlantSoortIdAndKenmerkId(plantSoortId, kenmerkId);
        if (bestaatAl) {
            throw new DuplicateResourceException(
                    "Koppeling bestaat al: plantSoortId=" + plantSoortId + ", kenmerkId=" + kenmerkId
            );
        }

        PlantKenmerk link = PlantKenmerk.builder()
                .plantSoort(plant)
                .kenmerk(kenmerk)
                .build();

        return plantKenmerkRepository.save(link);
    }

    public void removeKenmerkFromPlant(Long plantSoortId, Long kenmerkId) {
        // Check op bestaan, zodat je een nette 404 kunt geven
        PlantKenmerk link = plantKenmerkRepository.findByPlantSoortIdAndKenmerkId(plantSoortId, kenmerkId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Koppeling niet gevonden: plantSoortId=" + plantSoortId + ", kenmerkId=" + kenmerkId
                ));

        plantKenmerkRepository.delete(link);
    }
}
