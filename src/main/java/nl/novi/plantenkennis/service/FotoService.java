package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Foto;
import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.FotoRepository;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Service
public class FotoService {

    private final FotoRepository fotoRepository;
    private final PlantSoortRepository plantSoortRepository;
    private final FotoStorageService storageService;

    public FotoService(FotoRepository fotoRepository,
                       PlantSoortRepository plantSoortRepository,
                       FotoStorageService storageService) {
        this.fotoRepository = fotoRepository;
        this.plantSoortRepository = plantSoortRepository;
        this.storageService = storageService;
    }

    public List<Foto> getByPlantSoort(Long plantSoortId) {
        return fotoRepository.findByPlantSoortId(plantSoortId);
    }

    public Foto uploadToPlantSoort(Long plantSoortId,
                                   MultipartFile file,
                                   String altTekst,
                                   boolean hoofdfoto,
                                   String fotograaf,
                                   String licentie,
                                   String bron) {

        PlantSoort plant = plantSoortRepository.findById(plantSoortId)
                .orElseThrow(() -> new ResourceNotFoundException("PlantSoort niet gevonden: " + plantSoortId));

        // Als deze foto hoofdfoto wordt: zet bestaande hoofdfoto uit
        if (hoofdfoto) {
            fotoRepository.findFirstByPlantSoortIdAndHoofdfotoTrue(plantSoortId)
                    .ifPresent(bestaande -> {
                        bestaande.setHoofdfoto(false);
                        fotoRepository.save(bestaande);
                    });
        }

        FotoStorageService.StoredFile stored = storageService.store(plantSoortId, file);

        Foto foto = Foto.builder()
                .plantSoort(plant)
                .url(stored.url())
                .storagePath(stored.storagePath())
                .originalFilename(stored.originalFilename())
                .contentType(stored.contentType())
                .fileSize(stored.fileSize())
                .uploadedAt(Instant.now())
                .altTekst(altTekst)
                .hoofdfoto(hoofdfoto)
                .fotograaf(fotograaf)
                .licentie(licentie)
                .bron(bron)
                .build();

        return fotoRepository.save(foto);
    }

    public void deleteFoto(Long plantSoortId, Long fotoId) {

        Foto foto = fotoRepository.findByIdAndPlantSoortId(fotoId, plantSoortId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Foto niet gevonden: " + fotoId)
                );

        // 1️⃣ Verwijder bestand van filesystem
        storageService.delete(foto.getStoragePath());

        // 2️⃣ Verwijder metadata uit DB
        fotoRepository.delete(foto);
    }

}
