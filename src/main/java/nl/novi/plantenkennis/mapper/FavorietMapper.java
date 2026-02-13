package nl.novi.plantenkennis.mapper;

import nl.novi.plantenkennis.dto.FavorietResponseDto;
import nl.novi.plantenkennis.entity.Favoriet;

public class FavorietMapper {

    public static FavorietResponseDto toResponse(Favoriet entity) {
        return FavorietResponseDto.builder()
                .id(entity.getId())
                .gebruikerId(entity.getGebruiker().getId())
                .plantSoortId(entity.getPlantSoort().getId())
                .aangemaaktOp(entity.getAangemaaktOp())
                .build();
    }
}
