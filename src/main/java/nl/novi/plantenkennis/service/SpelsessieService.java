package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.dto.SpelsessieDto;
import nl.novi.plantenkennis.entity.Gebruiker;
import nl.novi.plantenkennis.entity.Spelsessie;
import nl.novi.plantenkennis.repository.GebruikerRepository;
import nl.novi.plantenkennis.repository.SpelsessieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpelsessieService {

    private final SpelsessieRepository spelsessieRepository;
    private final GebruikerRepository gebruikerRepository;

    public SpelsessieService(SpelsessieRepository spelsessieRepository,
                             GebruikerRepository gebruikerRepository) {
        this.spelsessieRepository = spelsessieRepository;
        this.gebruikerRepository = gebruikerRepository;
    }

    public List<SpelsessieDto> getAllSpelsessies() {
        return spelsessieRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<SpelsessieDto> getSpelsessiesByGebruikerId(Long gebruikerId) {
        return spelsessieRepository
                .findByGebruikerIdOrderByGespeeldOpDesc(gebruikerId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * spelsessie opslaan
     */
    public SpelsessieDto createSpelsessie(SpelsessieDto dto) {

        if (dto == null) {
            throw new IllegalArgumentException("SpelsessieDto mag niet null zijn.");
        }

        if (dto.getGebruikerId() == null) {
            throw new IllegalArgumentException("gebruikerId is verplicht.");
        }

        Gebruiker gebruiker = gebruikerRepository.findById(dto.getGebruikerId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Gebruiker niet gevonden met id: " + dto.getGebruikerId()));

        Spelsessie spelsessie = new Spelsessie(
                dto.getModus(),
                dto.getLevel(),
                dto.getScore(),
                dto.getDuurSec(),
                dto.getAantalCorrect(),
                dto.getAantalPogingen()
        );

        spelsessie.setGebruiker(gebruiker);

        Spelsessie saved = spelsessieRepository.save(spelsessie);

        return toDto(saved);
    }

    private SpelsessieDto toDto(Spelsessie s) {
        Long gebruikerId = (s.getGebruiker() != null) ? s.getGebruiker().getId() : null;

        return new SpelsessieDto(
                s.getId(),
                s.getModus(),
                s.getLevel(),
                s.getScore(),
                s.getDuurSec(),
                s.getAantalCorrect(),
                s.getAantalPogingen(),
                s.getGespeeldOp(),
                gebruikerId
        );
    }

    public void deleteSpelsessie(Long id) {
        if (!spelsessieRepository.existsById(id)) {
            throw new IllegalArgumentException("Spelsessie niet gevonden met id: " + id);
        }
        spelsessieRepository.deleteById(id);
    }

}
