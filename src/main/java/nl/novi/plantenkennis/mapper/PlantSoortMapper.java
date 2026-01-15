package nl.novi.plantenkennis.mapper;

import nl.novi.plantenkennis.dto.PlantSoortRequestDto;
import nl.novi.plantenkennis.dto.PlantSoortResponseDto;
import nl.novi.plantenkennis.entity.PlantSoort;

import java.time.LocalDateTime;

public class PlantSoortMapper {

    public static PlantSoort toEntity(PlantSoortRequestDto dto) {
        if (dto == null) return null;

        return PlantSoort.builder()
                .wetenschappelijkeNaam(dto.getWetenschappelijkeNaam())
                .nederlandseNaam(dto.getNederlandseNaam())
                .familie(dto.getFamilie())
                .beschrijving(dto.getBeschrijving())
                .bloeiperiodeStart(dto.getBloeiperiodeStart())
                .bloeiperiodeEinde(dto.getBloeiperiodeEinde())
                .giftig(dto.getGiftig())
                .inheems(dto.getInheems())
                .onderhoudsniveau(dto.getOnderhoudsniveau())
                .slug(generateSlug(dto.getNederlandseNaam()))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static PlantSoortResponseDto toResponse(PlantSoort entity) {
        if (entity == null) return null;

        return PlantSoortResponseDto.builder()
                .id(entity.getId())
                .wetenschappelijkeNaam(entity.getWetenschappelijkeNaam())
                .nederlandseNaam(entity.getNederlandseNaam())
                .familie(entity.getFamilie())
                .beschrijving(entity.getBeschrijving())
                .bloeiperiodeStart(entity.getBloeiperiodeStart())
                .bloeiperiodeEinde(entity.getBloeiperiodeEinde())
                .giftig(entity.getGiftig())
                .inheems(entity.getInheems())
                .onderhoudsniveau(entity.getOnderhoudsniveau())
                .slug(entity.getSlug())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private static String generateSlug(String naam) {
        if (naam == null) return null;
        return naam.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replace(" ", "-");
    }
}
