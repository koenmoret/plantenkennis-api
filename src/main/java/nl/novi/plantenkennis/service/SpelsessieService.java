package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.dto.SpelsessieDto;
import nl.novi.plantenkennis.entity.Spelsessie;
import nl.novi.plantenkennis.repository.SpelsessieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpelsessieService {

    private final SpelsessieRepository spelsessieRepository;

    public SpelsessieService(SpelsessieRepository spelsessieRepository) {
        this.spelsessieRepository = spelsessieRepository;
    }

    public List<SpelsessieDto> getAllSpelsessies() {
        return spelsessieRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
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

    public List<SpelsessieDto> getSpelsessiesByGebruikerId(Long gebruikerId) {
        return spelsessieRepository.findByGebruikerIdOrderByGespeeldOpDesc(gebruikerId)
                .stream()
                .map(this::toDto)
                .toList();
    }

}
