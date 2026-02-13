package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.dto.FavorietRequestDto;
import nl.novi.plantenkennis.dto.FavorietResponseDto;
import nl.novi.plantenkennis.mapper.FavorietMapper;
import nl.novi.plantenkennis.entity.Favoriet;
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

    // GET /favorieten?gebruikerId=1
    @GetMapping
    public List<FavorietResponseDto> getByGebruikerId(@RequestParam Long gebruikerId) {
        return service.getByGebruikerId(gebruikerId)
                .stream()
                .map(FavorietMapper::toResponse)
                .toList();
    }

    // POST /favorieten  { gebruikerId, plantSoortId }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavorietResponseDto create(@RequestBody FavorietRequestDto dto) {
        Favoriet created = service.create(dto.getGebruikerId(), dto.getPlantSoortId());
        return FavorietMapper.toResponse(created);
    }


    // DELETE /favorieten?gebruikerId=1&plantSoortId=2
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Long gebruikerId, @RequestParam Long plantSoortId) {
        service.delete(gebruikerId, plantSoortId);
    }
}
