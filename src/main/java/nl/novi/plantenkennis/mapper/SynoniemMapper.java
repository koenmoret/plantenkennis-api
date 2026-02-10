package nl.novi.plantenkennis.mapper;

import nl.novi.plantenkennis.dto.SynoniemResponseDto;
import nl.novi.plantenkennis.entity.Synoniem;

public class SynoniemMapper {

    public static SynoniemResponseDto toResponse(Synoniem s) {
        return SynoniemResponseDto.builder()
                .id(s.getId())
                .naam(s.getNaam())
                .build();
    }
}
