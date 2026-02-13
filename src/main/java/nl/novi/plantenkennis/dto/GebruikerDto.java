package nl.novi.plantenkennis.dto;

import java.time.LocalDateTime;

public class GebruikerDto {

    private Long id;
    private String naam;
    private String email;
    private LocalDateTime createdAt;

    // optioneel (handig in responses)
    private Integer aantalSpelsessies;

    public GebruikerDto() {}

    public GebruikerDto(Long id, String naam, String email, LocalDateTime createdAt, Integer aantalSpelsessies) {
        this.id = id;
        this.naam = naam;
        this.email = email;
        this.createdAt = createdAt;
        this.aantalSpelsessies = aantalSpelsessies;
    }

    public Long getId() { return id; }
    public String getNaam() { return naam; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Integer getAantalSpelsessies() { return aantalSpelsessies; }

    public void setId(Long id) { this.id = id; }
    public void setNaam(String naam) { this.naam = naam; }
    public void setEmail(String email) { this.email = email; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setAantalSpelsessies(Integer aantalSpelsessies) { this.aantalSpelsessies = aantalSpelsessies; }
}
