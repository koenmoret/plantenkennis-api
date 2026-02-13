package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.dto.SpelsessieDto;
import nl.novi.plantenkennis.service.SpelsessieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spelsessies")
public class SpelsessieController {

    private final SpelsessieService spelsessieService;

    public SpelsessieController(SpelsessieService spelsessieService) {
        this.spelsessieService = spelsessieService;
    }

    @GetMapping
    public List<SpelsessieDto> getAllSpelsessies() {
        return spelsessieService.getAllSpelsessies();
    }

    @GetMapping("/gebruikers/{id}/spelsessies")
    public List<SpelsessieDto> getSpelsessiesByGebruiker(@PathVariable Long id) {
        return spelsessieService.getSpelsessiesByGebruikerId(id);
    }
}

