package nl.novi.plantenkennis.dto;

import java.time.LocalDateTime;

public class GebruikerDto {

    private Long id;
    private String keycloakSubject;
    private String naam;
    private String email;
    private LocalDateTime createdAt;
    private Integer aantalSpelsessies;

    // Lege constructor (nodig voor Jackson)
    public GebruikerDto() {
    }

    // All-args constructor
    public GebruikerDto(Long id,
                        String keycloakSubject,
                        String naam,
                        String email,
                        LocalDateTime createdAt,
                        Integer aantalSpelsessies) {
        this.id = id;
        this.keycloakSubject = keycloakSubject;
        this.naam = naam;
        this.email = email;
        this.createdAt = createdAt;
        this.aantalSpelsessies = aantalSpelsessies;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getKeycloakSubject() {
        return keycloakSubject;
    }

    public String getNaam() {
        return naam;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getAantalSpelsessies() {
        return aantalSpelsessies;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setKeycloakSubject(String keycloakSubject) {
        this.keycloakSubject = keycloakSubject;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setAantalSpelsessies(Integer aantalSpelsessies) {
        this.aantalSpelsessies = aantalSpelsessies;
    }
}
