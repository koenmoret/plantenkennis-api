package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.entity.PlantSoort;
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

    @GetMapping
    public ResponseEntity<List<PlantSoort>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<PlantSoort> create(@RequestBody PlantSoort plantSoort) {
        PlantSoort created = service.create(plantSoort);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
