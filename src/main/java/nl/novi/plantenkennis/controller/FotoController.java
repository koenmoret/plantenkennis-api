package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.dto.FotoResponseDto;
import nl.novi.plantenkennis.mapper.FotoMapper;
import nl.novi.plantenkennis.service.FotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/plantsoorten/{plantSoortId}/fotos")
public class FotoController {

    private final FotoService service;

    public FotoController(FotoService service) {
        this.service = service;
    }

    @GetMapping
    public List<FotoResponseDto> getByPlantSoort(@PathVariable Long plantSoortId) {
        return service.getByPlantSoort(plantSoortId)
                .stream()
                .map(FotoMapper::toResponse)
                .toList();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FotoResponseDto upload(
            @PathVariable Long plantSoortId,
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) String altTekst,
            @RequestParam(defaultValue = "false") boolean hoofdfoto,
            @RequestParam(required = false) String fotograaf,
            @RequestParam(required = false) String licentie,
            @RequestParam(required = false) String bron
    ) {
        var created = service.uploadToPlantSoort(
                plantSoortId, file, altTekst, hoofdfoto, fotograaf, licentie, bron
        );
        return FotoMapper.toResponse(created);
    }

    @DeleteMapping("/{fotoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long plantSoortId,
            @PathVariable Long fotoId
    ) {
        service.deleteFoto(plantSoortId, fotoId);
    }

}
