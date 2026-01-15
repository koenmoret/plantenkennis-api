package nl.novi.plantenkennis.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PlantSoortResponseDto {

    private Long id;

    private String wetenschappelijkeNaam;
    private String nederlandseNaam;
    private String familie;
    private String beschrijving;

    private Integer bloeiperiodeStart;
    private Integer bloeiperiodeEinde;

    private Boolean giftig;
    private Boolean inheems;

    private String onderhoudsniveau;

    private String slug;
    private LocalDateTime updatedAt;
}
