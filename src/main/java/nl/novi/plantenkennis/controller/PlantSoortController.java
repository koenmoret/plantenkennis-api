package nl.novi.plantenkennis.controller;

import jakarta.validation.Valid;
import nl.novi.plantenkennis.dto.KenmerkResponseDto;
import nl.novi.plantenkennis.dto.PlantSoortRequestDto;
import nl.novi.plantenkennis.dto.PlantSoortResponseDto;
import nl.novi.plantenkennis.dto.SynoniemRequestDto;
import nl.novi.plantenkennis.dto.SynoniemResponseDto;
import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.mapper.KenmerkMapper;
import nl.novi.plantenkennis.mapper.PlantSoortMapper;
import nl.novi.plantenkennis.mapper.SynoniemMapper;
import nl.novi.plantenkennis.service.PlantKenmerkService;
import nl.novi.plantenkennis.service.PlantSoortService;
import nl.novi.plantenkennis.service.SynoniemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plantsoorten")
public class PlantSoortController {

    private final PlantSoortService service;
    private final PlantKenmerkService plantKenmerkService;
    private final SynoniemService synoniemService;

    public PlantSoortController(
            PlantSoortService service,
            PlantKenmerkService plantKenmerkService,
            SynoniemService synoniemService
    ) {
        this.service = service;
        this.plantKenmerkService = plantKenmerkService;
        this.synoniemService = synoniemService;
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
    public ResponseEntity<PlantSoortResponseDto> create(@Valid @RequestBody PlantSoortRequestDto dto) {
        PlantSoort created = service.create(PlantSoortMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(PlantSoortMapper.toResponse(created));
    }

    @GetMapping("/{plantSoortId}/kenmerken")
    public ResponseEntity<List<KenmerkResponseDto>> getKenmerken(@PathVariable Long plantSoortId) {
        List<KenmerkResponseDto> response = plantKenmerkService.getKenmerkenVoorPlant(plantSoortId).stream()
                .map(KenmerkMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{plantSoortId}/kenmerken/{kenmerkId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addKenmerk(@PathVariable Long plantSoortId, @PathVariable Long kenmerkId) {
        plantKenmerkService.addKenmerkToPlant(plantSoortId, kenmerkId);
    }

    @DeleteMapping("/{plantSoortId}/kenmerken/{kenmerkId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeKenmerk(@PathVariable Long plantSoortId, @PathVariable Long kenmerkId) {
        plantKenmerkService.removeKenmerkFromPlant(plantSoortId, kenmerkId);
    }

    @GetMapping("/{plantSoortId}/synoniemen")
    public ResponseEntity<List<SynoniemResponseDto>> getSynoniemen(@PathVariable Long plantSoortId) {
        List<SynoniemResponseDto> response = synoniemService.getByPlantSoort(plantSoortId).stream()
                .map(SynoniemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{plantSoortId}/synoniemen")
    @ResponseStatus(HttpStatus.CREATED)
    public SynoniemResponseDto addSynoniem(
            @PathVariable Long plantSoortId,
            @Valid @RequestBody SynoniemRequestDto dto
    ) {
        return SynoniemMapper.toResponse(synoniemService.addToPlant(plantSoortId, dto.getNaam()));
    }

    @DeleteMapping("/{plantSoortId}/synoniemen/{synoniemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSynoniem(@PathVariable Long plantSoortId, @PathVariable Long synoniemId) {
        synoniemService.removeFromPlant(plantSoortId, synoniemId);
    }

    /**
     * Zoeken/filteren op kolommen van plant_soorten (+ paging)
     *
     * Voorbeelden:
     *  - /plantsoorten/search?q=lav
     *  - /plantsoorten/search?familie=Lamiaceae&inheems=false
     *  - /plantsoorten/search?giftig=true&onderhoudsniveau=GEMIDDELD
     *  - /plantsoorten/search?bloeimaand=4
     */
    @GetMapping("/search")
    public ResponseEntity<Page<PlantSoortResponseDto>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String familie,
            @RequestParam(required = false) Boolean giftig,
            @RequestParam(required = false) Boolean inheems,
            @RequestParam(required = false) String onderhoudsniveau,
            @RequestParam(required = false) String slug,
            @RequestParam(required = false) Integer bloeimaand,
            @RequestParam(required = false) String updatedAfter,   // ISO: 2026-02-01T00:00:00
            @RequestParam(required = false) String updatedBefore,  // ISO
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<PlantSoortResponseDto> response = service
                .searchTableFields(q, familie, giftig, inheems, onderhoudsniveau, slug, bloeimaand, updatedAfter, updatedBefore, pageable)
                .map(PlantSoortMapper::toResponse);

        return ResponseEntity.ok(response);
    }
}
