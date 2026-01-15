package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Favoriet;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.repository.FavorietRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavorietService {

    private final FavorietRepository repository;

    public FavorietService(FavorietRepository repository) {
        this.repository = repository;
    }

    public List<Favoriet> getByGebruikerId(Long gebruikerId) {
        return repository.findByGebruikerId(gebruikerId);
    }

    public Favoriet create(Favoriet favoriet) {
        boolean bestaatAl = repository.existsByGebruikerIdAndPlantSoortId(
                favoriet.getGebruikerId(),
                favoriet.getPlantSoortId()
        );

        if (bestaatAl) {
            throw new DuplicateResourceException(
                    "Favoriet bestaat al: gebruikerId=" + favoriet.getGebruikerId() +
                            ", plantSoortId=" + favoriet.getPlantSoortId()
            );
        }

        return repository.save(favoriet);
    }
}
