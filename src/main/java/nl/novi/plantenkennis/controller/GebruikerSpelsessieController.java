package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.dto.SpelsessieDto;
import nl.novi.plantenkennis.service.SpelsessieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gebruikers")
public class GebruikerSpelsessieController {

    private final SpelsessieService spelsessieService;

    public GebruikerSpelsessieController(SpelsessieService spelsessieService) {
        this.spelsessieService = spelsessieService;
    }

    @GetMapping("/{id}/spelsessies")
    public List<SpelsessieDto> getSpelsessiesByGebruiker(@PathVariable Long id) {
        return spelsessieService.getSpelsessiesByGebruikerId(id);
    }
}
