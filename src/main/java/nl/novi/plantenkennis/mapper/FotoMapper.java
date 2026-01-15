package nl.novi.plantenkennis.mapper;

import nl.novi.plantenkennis.dto.FotoRequestDto;
import nl.novi.plantenkennis.dto.FotoResponseDto;
import nl.novi.plantenkennis.entity.Foto;
import nl.novi.plantenkennis.entity.PlantSoort;

public class FotoMapper {

    public static Foto toEntity(FotoRequestDto dto, PlantSoort plantSoort) {
        if (dto == null) return null;

        return Foto.builder()
                .url(dto.getUrl())
                .fotograaf(dto.getFotograaf())
                .licentie(dto.getLicentie())
                .altTekst(dto.getAltTekst())
                .hoofdfoto(dto.getHoofdfoto())
                .bron(dto.getBron())
                .plantSoort(plantSoort)
                .build();
    }

    public static FotoResponseDto toResponse(Foto entity) {
        if (entity == null) return null;

        return FotoResponseDto.builder()
                .id(entity.getId())
                .url(entity.getUrl())
                .fotograaf(entity.getFotograaf())
                .licentie(entity.getLicentie())
                .altTekst(entity.getAltTekst())
                .hoofdfoto(entity.getHoofdfoto())
                .bron(entity.getBron())
                .build();
    }
}
