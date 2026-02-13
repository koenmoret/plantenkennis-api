package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.dto.GebruikerDto;
import nl.novi.plantenkennis.entity.Gebruiker;
import nl.novi.plantenkennis.repository.GebruikerRepository;
import nl.novi.plantenkennis.repository.SpelsessieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GebruikerService {

    private final GebruikerRepository gebruikerRepository;
    private final SpelsessieRepository spelsessieRepository;

    public GebruikerService(GebruikerRepository gebruikerRepository,
                            SpelsessieRepository spelsessieRepository) {
        this.gebruikerRepository = gebruikerRepository;
        this.spelsessieRepository = spelsessieRepository;
    }

    public List<GebruikerDto> getAllGebruikers() {
        return gebruikerRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public GebruikerDto getGebruikerById(Long id) {
        Gebruiker gebruiker = gebruikerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gebruiker met id " + id + " niet gevonden"));

        return toDto(gebruiker);
    }

    private GebruikerDto toDto(Gebruiker g) {
        int aantalSpelsessies = (int) spelsessieRepository.countByGebruikerId(g.getId());

        return new GebruikerDto(
                g.getId(),
                g.getNaam(),
                g.getEmail(),
                g.getCreatedAt(),
                aantalSpelsessies
        );
    }
}
