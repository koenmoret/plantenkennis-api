package nl.novi.plantenkennis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlantSoortRequestDto {

    @NotBlank
    @Size(max = 120)
    private String naam;

    @Size(max = 120)
    private String latijnseNaam;

    @Size(max = 2000)
    private String omschrijving;
}
