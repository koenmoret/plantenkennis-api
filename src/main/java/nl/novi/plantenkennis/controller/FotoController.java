package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.dto.FotoRequestDto;
import nl.novi.plantenkennis.dto.FotoResponseDto;
import nl.novi.plantenkennis.entity.Foto;
import nl.novi.plantenkennis.mapper.FotoMapper;
import nl.novi.plantenkennis.service.FotoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FotoResponseDto add(@PathVariable Long plantSoortId, @RequestBody FotoRequestDto dto) {
        Foto foto = FotoMapper.toEntity(dto, null);
        Foto created = service.addToPlantSoort(plantSoortId, foto);
        return FotoMapper.toResponse(created);
    }
}
