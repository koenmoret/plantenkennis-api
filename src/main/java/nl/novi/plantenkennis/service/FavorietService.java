package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Favoriet;
import nl.novi.plantenkennis.entity.Gebruiker;
import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.FavorietRepository;
import nl.novi.plantenkennis.repository.GebruikerRepository;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavorietService {

    private final FavorietRepository favorietRepository;
    private final GebruikerRepository gebruikerRepository;
    private final PlantSoortRepository plantSoortRepository;

    public FavorietService(FavorietRepository favorietRepository,
                           GebruikerRepository gebruikerRepository,
                           PlantSoortRepository plantSoortRepository) {
        this.favorietRepository = favorietRepository;
        this.gebruikerRepository = gebruikerRepository;
        this.plantSoortRepository = plantSoortRepository;
    }

    public List<Favoriet> getByGebruikerId(Long gebruikerId) {
        return favorietRepository.findByGebruiker_Id(gebruikerId);
    }

    @Transactional
    public Favoriet create(Long gebruikerId, Long plantSoortId) {

        if (favorietRepository.existsByGebruiker_IdAndPlantSoort_Id(gebruikerId, plantSoortId)) {
            throw new DuplicateResourceException(
                    "Favoriet bestaat al: gebruikerId=" + gebruikerId + ", plantSoortId=" + plantSoortId
            );
        }

        Gebruiker gebruiker = gebruikerRepository.findById(gebruikerId)
                .orElseThrow(() -> new RuntimeException("Gebruiker niet gevonden: " + gebruikerId));

        PlantSoort plantSoort = plantSoortRepository.findById(plantSoortId)
                .orElseThrow(() -> new RuntimeException("PlantSoort niet gevonden: " + plantSoortId));

        Favoriet favoriet = Favoriet.builder()
                .gebruiker(gebruiker)
                .plantSoort(plantSoort)
                .build();

        return favorietRepository.save(favoriet);
    }

    @Transactional
    public void delete(Long gebruikerId, Long plantSoortId) {
        Favoriet favoriet = favorietRepository
                .findByGebruiker_IdAndPlantSoort_Id(gebruikerId, plantSoortId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Favoriet niet gevonden: gebruikerId=" + gebruikerId + ", plantSoortId=" + plantSoortId
                ));

        favorietRepository.delete(favoriet);
    }
}

