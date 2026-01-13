package nl.novi.plantenkennis.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlantSoortResponseDto {
    Long id;
    String naam;
    String latijnseNaam;
    String omschrijving;
}
