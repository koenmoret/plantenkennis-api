package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Foto;
import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.FotoRepository;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FotoService {

    private final FotoRepository fotoRepository;
    private final PlantSoortRepository plantSoortRepository;

    public FotoService(FotoRepository fotoRepository, PlantSoortRepository plantSoortRepository) {
        this.fotoRepository = fotoRepository;
        this.plantSoortRepository = plantSoortRepository;
    }

    public List<Foto> getByPlantSoort(Long plantSoortId) {
        return fotoRepository.findByPlantSoortId(plantSoortId);
    }

    public Foto addToPlantSoort(Long plantSoortId, Foto foto) {
        PlantSoort plant = plantSoortRepository.findById(plantSoortId)
                .orElseThrow(() -> new ResourceNotFoundException("PlantSoort niet gevonden: " + plantSoortId));

        foto.setPlantSoort(plant);
        return fotoRepository.save(foto);
    }
}
