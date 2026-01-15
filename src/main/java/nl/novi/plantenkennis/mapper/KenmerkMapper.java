package nl.novi.plantenkennis.mapper;

import nl.novi.plantenkennis.dto.KenmerkRequestDto;
import nl.novi.plantenkennis.dto.KenmerkResponseDto;
import nl.novi.plantenkennis.entity.Kenmerk;

public class KenmerkMapper {

    public static Kenmerk toEntity(KenmerkRequestDto dto) {
        return Kenmerk.builder()
                .type(dto.getType())
                .waarde(dto.getWaarde())
                .build();
    }

    public static KenmerkResponseDto toResponse(Kenmerk entity) {
        return KenmerkResponseDto.builder()
                .id(entity.getId())
                .type(entity.getType())
                .waarde(entity.getWaarde())
                .build();
    }
}
