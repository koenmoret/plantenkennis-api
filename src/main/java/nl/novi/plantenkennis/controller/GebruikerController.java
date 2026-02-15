package nl.novi.plantenkennis.controller;

import jakarta.validation.Valid;
import nl.novi.plantenkennis.dto.GebruikerDto;
import nl.novi.plantenkennis.dto.GebruikerUpdateDto;
import nl.novi.plantenkennis.service.GebruikerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gebruikers")
public class GebruikerController {

    private final GebruikerService gebruikerService;

    public GebruikerController(GebruikerService gebruikerService) {
        this.gebruikerService = gebruikerService;
    }

    @GetMapping
    public List<GebruikerDto> getAllGebruikers() {
        return gebruikerService.getAllGebruikers();
    }

    @GetMapping("/{id}")
    public GebruikerDto getGebruikerById(@PathVariable Long id) {
        return gebruikerService.getGebruikerById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_client_admin') or hasAuthority('ROLE_client_deelnemer')")
    public GebruikerDto updateGebruiker(
            @PathVariable Long id,
            @Valid @RequestBody GebruikerUpdateDto dto,
            JwtAuthenticationToken auth
    ) {
        return gebruikerService.updateGebruiker(id, dto, auth);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_client_admin')")
    public void deleteGebruiker(@PathVariable Long id) {
        gebruikerService.deleteGebruiker(id);
    }
}
