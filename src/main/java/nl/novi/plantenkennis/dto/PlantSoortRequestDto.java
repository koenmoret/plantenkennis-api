package nl.novi.plantenkennis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlantSoortRequestDto {

    private String wetenschappelijkeNaam;
    private String nederlandseNaam;
    private String familie;
    private String beschrijving;

    private Integer bloeiperiodeStart;
    private Integer bloeiperiodeEinde;

    private Boolean giftig;
    private Boolean inheems;

    private String onderhoudsniveau;
}
