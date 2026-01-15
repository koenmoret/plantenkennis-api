package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.dto.KenmerkRequestDto;
import nl.novi.plantenkennis.dto.KenmerkResponseDto;
import nl.novi.plantenkennis.entity.Kenmerk;
import nl.novi.plantenkennis.mapper.KenmerkMapper;
import nl.novi.plantenkennis.service.KenmerkService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kenmerken")
public class KenmerkController {

    private final KenmerkService service;

    public KenmerkController(KenmerkService service) {
        this.service = service;
    }

    @GetMapping({"", "/"})
    public List<KenmerkResponseDto> getAll() {
        return service.getAll()
                .stream()
                .map(KenmerkMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public KenmerkResponseDto getById(@PathVariable Long id) {
        Kenmerk kenmerk = service.getById(id);
        return KenmerkMapper.toResponse(kenmerk);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KenmerkResponseDto create(@RequestBody KenmerkRequestDto dto) {
        Kenmerk kenmerk = KenmerkMapper.toEntity(dto);
        Kenmerk created = service.create(kenmerk);
        return KenmerkMapper.toResponse(created);
    }
}
