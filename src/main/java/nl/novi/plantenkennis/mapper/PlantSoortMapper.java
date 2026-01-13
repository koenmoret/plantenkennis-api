package nl.novi.plantenkennis.mapper;

import nl.novi.plantenkennis.dto.PlantSoortRequestDto;
import nl.novi.plantenkennis.dto.PlantSoortResponseDto;
import nl.novi.plantenkennis.entity.PlantSoort;

public class PlantSoortMapper {

    public static PlantSoort toEntity(PlantSoortRequestDto dto) {
        return PlantSoort.builder()
                .naam(dto.getNaam())
                .latijnseNaam(dto.getLatijnseNaam())
                .omschrijving(dto.getOmschrijving())
                .build();
    }

    public static PlantSoortResponseDto toResponse(PlantSoort entity) {
        return PlantSoortResponseDto.builder()
                .id(entity.getId())
                .naam(entity.getNaam())
                .latijnseNaam(entity.getLatijnseNaam())
                .omschrijving(entity.getOmschrijving())
                .build();
    }
}
