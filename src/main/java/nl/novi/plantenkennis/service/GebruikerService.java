package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.dto.GebruikerDto;
import nl.novi.plantenkennis.dto.GebruikerUpdateDto;
import nl.novi.plantenkennis.entity.Gebruiker;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.GebruikerRepository;
import nl.novi.plantenkennis.repository.SpelsessieRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
                .orElseThrow(() ->
                        new ResourceNotFoundException("Gebruiker met id " + id + " niet gevonden"));

        return toDto(gebruiker);
    }

    public GebruikerDto updateGebruiker(Long id,
                                        GebruikerUpdateDto dto,
                                        JwtAuthenticationToken auth) {

        Gebruiker gebruiker = gebruikerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Gebruiker met id " + id + " niet gevonden"));

        boolean isAdmin = auth.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_client_admin"));

        if (!isAdmin) {
            String subject = auth.getToken().getSubject();

            if (subject == null || !subject.equals(gebruiker.getKeycloakSubject())) {
                throw new AccessDeniedException("Je mag alleen je eigen gebruiker wijzigen.");
            }
        }

        gebruiker.setNaam(dto.getNaam());

        Gebruiker saved = gebruikerRepository.save(gebruiker);
        return toDto(saved);
    }

    public void deleteGebruiker(Long id) {
        Gebruiker gebruiker = gebruikerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Gebruiker met id " + id + " niet gevonden"));

        gebruikerRepository.delete(gebruiker);
    }

    private GebruikerDto toDto(Gebruiker g) {
        int aantalSpelsessies =
                (int) spelsessieRepository.countByGebruikerId(g.getId());

        return new GebruikerDto(
                g.getId(),
                g.getKeycloakSubject(),
                g.getNaam(),
                g.getEmail(),
                g.getCreatedAt(),
                aantalSpelsessies
        );
    }
}
