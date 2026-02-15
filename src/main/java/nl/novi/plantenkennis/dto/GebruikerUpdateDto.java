package nl.novi.plantenkennis.dto;

import jakarta.validation.constraints.NotBlank;

public class GebruikerUpdateDto {

    @NotBlank
    private String naam;

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
}
