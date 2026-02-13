package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.dto.SpelsessieDto;
import nl.novi.plantenkennis.service.SpelsessieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spelsessies")
public class SpelsessieController {

    private final SpelsessieService spelsessieService;

    public SpelsessieController(SpelsessieService spelsessieService) {
        this.spelsessieService = spelsessieService;
    }

    /**
     * Alle spelsessies ophalen
     */
    @GetMapping
    public ResponseEntity<List<SpelsessieDto>> getAllSpelsessies() {
        return ResponseEntity.ok(spelsessieService.getAllSpelsessies());
    }

    /**
     * Spelsessies van specifieke gebruiker ophalen
     */
    @GetMapping("/gebruikers/{id}")
    public ResponseEntity<List<SpelsessieDto>> getSpelsessiesByGebruiker(@PathVariable Long id) {
        return ResponseEntity.ok(spelsessieService.getSpelsessiesByGebruikerId(id));
    }

    /**
     * spelsessie opslaan (memory afronden)
     */
    @PostMapping
    public ResponseEntity<SpelsessieDto> createSpelsessie(@RequestBody SpelsessieDto dto) {
        SpelsessieDto created = spelsessieService.createSpelsessie(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * spelsessie verwijderen (Admin)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpelsessie(@PathVariable Long id) {
        spelsessieService.deleteSpelsessie(id);
        return ResponseEntity.noContent().build();
    }

}
