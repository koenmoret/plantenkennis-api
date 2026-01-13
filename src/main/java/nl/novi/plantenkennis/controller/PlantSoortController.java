package nl.novi.plantenkennis.controller;

import jakarta.validation.Valid;
import nl.novi.plantenkennis.dto.PlantSoortRequestDto;
import nl.novi.plantenkennis.dto.PlantSoortResponseDto;
import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.mapper.PlantSoortMapper;
import nl.novi.plantenkennis.service.PlantSoortService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plantsoorten")
public class PlantSoortController {

    private final PlantSoortService service;

    public PlantSoortController(PlantSoortService service) {
        this.service = service;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<PlantSoortResponseDto>> getAll() {
        List<PlantSoortResponseDto> response = service.getAll().stream()
                .map(PlantSoortMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantSoortResponseDto> getById(@PathVariable Long id) {
        PlantSoort plantSoort = service.getById(id);
        return ResponseEntity.ok(PlantSoortMapper.toResponse(plantSoort));
    }

    @PostMapping
    public ResponseEntity<PlantSoortResponseDto> create(
            @Valid @RequestBody PlantSoortRequestDto dto) {

        PlantSoort created = service.create(PlantSoortMapper.toEntity(dto));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PlantSoortMapper.toResponse(created));
    }
}
