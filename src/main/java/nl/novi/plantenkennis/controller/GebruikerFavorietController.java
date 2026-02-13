package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.dto.FavorietRequestDto;
import nl.novi.plantenkennis.dto.FavorietResponseDto;
import nl.novi.plantenkennis.entity.Favoriet;
import nl.novi.plantenkennis.mapper.FavorietMapper;
import nl.novi.plantenkennis.service.FavorietService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gebruikers")
public class GebruikerFavorietController {

    private final FavorietService service;

    public GebruikerFavorietController(FavorietService service) {
        this.service = service;
    }

    // GET /gebruikers/{id}/favorieten
    @GetMapping("/{id}/favorieten")
    public List<FavorietResponseDto> getFavorieten(@PathVariable Long id) {
        return service.getByGebruikerId(id)
                .stream()
                .map(FavorietMapper::toResponse)
                .toList();
    }

    // POST /gebruikers/{id}/favorieten
    @PostMapping("/{id}/favorieten")
    @ResponseStatus(HttpStatus.CREATED)
    public FavorietResponseDto addFavoriet(
            @PathVariable Long id,
            @RequestBody FavorietRequestDto dto
    ) {
        Favoriet created = service.create(id, dto.getPlantSoortId());
        return FavorietMapper.toResponse(created);
    }

    // DELETE /gebruikers/{gebruikerId}/favorieten/{plantSoortId}
    @DeleteMapping("/{gebruikerId}/favorieten/{plantSoortId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavoriet(@PathVariable Long gebruikerId,
                               @PathVariable Long plantSoortId) {
        service.delete(gebruikerId, plantSoortId);
    }
}
