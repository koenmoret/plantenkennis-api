package nl.novi.plantenkennis.mapper;

import nl.novi.plantenkennis.dto.FavorietRequestDto;
import nl.novi.plantenkennis.dto.FavorietResponseDto;
import nl.novi.plantenkennis.entity.Favoriet;

public class FavorietMapper {

    public static Favoriet toEntity(FavorietRequestDto dto) {
        return Favoriet.builder()
                .gebruikerId(dto.getGebruikerId())
                .plantSoortId(dto.getPlantSoortId())
                .build();
    }

    public static FavorietResponseDto toResponse(Favoriet entity) {
        return FavorietResponseDto.builder()
                .id(entity.getId())
                .gebruikerId(entity.getGebruikerId())
                .plantSoortId(entity.getPlantSoortId())
                .build();
    }
}
