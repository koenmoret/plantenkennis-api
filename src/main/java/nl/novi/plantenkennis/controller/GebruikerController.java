package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.dto.GebruikerDto;
import nl.novi.plantenkennis.service.GebruikerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gebruikers")
public class GebruikerController {

    private final GebruikerService gebruikerService;

    public GebruikerController(GebruikerService gebruikerService) {
        this.gebruikerService = gebruikerService;
    }

    // GET /gebruikers
    @GetMapping
    public List<GebruikerDto> getAllGebruikers() {
        return gebruikerService.getAllGebruikers();
    }

    // GET /gebruikers/{id}
    @GetMapping("/{id}")
    public GebruikerDto getGebruikerById(@PathVariable Long id) {
        return gebruikerService.getGebruikerById(id);
    }
}


