package nl.novi.plantenkennis.mapper;

import nl.novi.plantenkennis.dto.FotoResponseDto;
import nl.novi.plantenkennis.entity.Foto;

public class FotoMapper {

    public static FotoResponseDto toResponse(Foto entity) {
        if (entity == null) return null;

        return FotoResponseDto.builder()
                .id(entity.getId())
                .url(entity.getUrl())
                .storagePath(entity.getStoragePath())
                .originalFilename(entity.getOriginalFilename())
                .contentType(entity.getContentType())
                .fileSize(entity.getFileSize())
                .uploadedAt(entity.getUploadedAt())
                .fotograaf(entity.getFotograaf())
                .licentie(entity.getLicentie())
                .altTekst(entity.getAltTekst())
                .hoofdfoto(entity.isHoofdfoto())
                .bron(entity.getBron())
                .build();
    }
}
