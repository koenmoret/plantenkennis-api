package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.dto.FavorietRequestDto;
import nl.novi.plantenkennis.dto.FavorietResponseDto;
import nl.novi.plantenkennis.entity.Favoriet;
import nl.novi.plantenkennis.mapper.FavorietMapper;
import nl.novi.plantenkennis.service.FavorietService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorieten")
public class FavorietController {

    private final FavorietService service;

    public FavorietController(FavorietService service) {
        this.service = service;
    }

    @GetMapping
    public List<FavorietResponseDto> getByGebruikerId(@RequestParam Long gebruikerId) {
        return service.getByGebruikerId(gebruikerId)
                .stream()
                .map(FavorietMapper::toResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavorietResponseDto create(@RequestBody FavorietRequestDto dto) {
        Favoriet favoriet = FavorietMapper.toEntity(dto);
        Favoriet created = service.create(favoriet);
        return FavorietMapper.toResponse(created);
    }
}
