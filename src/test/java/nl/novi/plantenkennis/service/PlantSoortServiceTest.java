package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantSoortServiceTest {

    @Mock
    private PlantSoortRepository repository;

    @InjectMocks
    private PlantSoortService service;

    @DisplayName("getAll() geeft alle plantsoorten terug")
    @Test
    void getAll_happyFlow_returnsListOfPlantSoorten() {
        // Arrange
        PlantSoort tulp = PlantSoort.builder()
                .id(1L)
                .nederlandseNaam("Tulp")
                .wetenschappelijkeNaam("Tulipa gesneriana")
                .familie("Liliaceae")
                .beschrijving("Voorjaarsbol met opvallende bloemen.")
                .bloeiperiodeStart(4)
                .bloeiperiodeEinde(5)
                .giftig(true)
                .inheems(false)
                .onderhoudsniveau("LAAG")
                .slug("tulp")
                .updatedAt(LocalDateTime.now())
                .build();

        PlantSoort roos = PlantSoort.builder()
                .id(2L)
                .nederlandseNaam("Hondsroos")
                .wetenschappelijkeNaam("Rosa rubiginosa")
                .familie("Rosaceae")
                .beschrijving("Wilde roos met bottels.")
                .bloeiperiodeStart(6)
                .bloeiperiodeEinde(7)
                .giftig(false)
                .inheems(true)
                .onderhoudsniveau("GEMIDDELD")
                .slug("hondsroos")
                .updatedAt(LocalDateTime.now())
                .build();

        when(repository.findAll()).thenReturn(List.of(tulp, roos));

        // Act
        List<PlantSoort> result = service.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Tulp", result.get(0).getNederlandseNaam());
        assertEquals("Hondsroos", result.get(1).getNederlandseNaam());
        verify(repository).findAll();
    }

    @DisplayName("getById() geeft één plantsoort terug wanneer deze bestaat")
    @Test
    void getById_happyFlow_returnsPlantSoort() {
        // Arrange
        PlantSoort tulp = PlantSoort.builder()
                .id(1L)
                .nederlandseNaam("Tulp")
                .wetenschappelijkeNaam("Tulipa gesneriana")
                .beschrijving("Voorjaarsbol met opvallende bloemen.")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(tulp));

        // Act
        PlantSoort result = service.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Tulp", result.getNederlandseNaam());
        verify(repository).findById(1L);
    }

    @DisplayName("getById() gooit ResourceNotFoundException wanneer plantsoort niet bestaat")
    @Test
    void getById_notFound_throwsResourceNotFoundException() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getById(99L)
        );

        assertTrue(ex.getMessage().contains("PlantSoort niet gevonden"));
        verify(repository).findById(99L);
    }

    @DisplayName("create() slaat plantsoort op wanneer nederlandseNaam nog niet bestaat en zet updatedAt")
    @Test
    void create_happyFlow_savesPlantSoort_andSetsUpdatedAt() {
        // Arrange
        PlantSoort nieuw = PlantSoort.builder()
                .nederlandseNaam("Lavendel")
                .wetenschappelijkeNaam("Lavandula angustifolia")
                .beschrijving("Geurende paarse bloeier.")
                .build();

        when(repository.existsByNederlandseNaamIgnoreCase("Lavendel")).thenReturn(false);

        // Save retourneert vaak dezelfde entity (met id). We simuleren dat hier.
        PlantSoort saved = PlantSoort.builder()
                .id(10L)
                .nederlandseNaam("Lavendel")
                .wetenschappelijkeNaam("Lavandula angustifolia")
                .beschrijving("Geurende paarse bloeier.")
                .updatedAt(LocalDateTime.now())
                .build();

        when(repository.save(any(PlantSoort.class))).thenReturn(saved);

        // Act
        PlantSoort result = service.create(nieuw);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Lavendel", result.getNederlandseNaam());

        // belangrijk: service zet updatedAt op het object vóór save()
        assertNotNull(nieuw.getUpdatedAt(), "updatedAt moet door de service gezet worden");

        verify(repository).existsByNederlandseNaamIgnoreCase("Lavendel");
        verify(repository).save(nieuw);
    }

    @DisplayName("create() gooit DuplicateResourceException wanneer nederlandseNaam al bestaat")
    @Test
    void create_duplicate_throwsDuplicateResourceException() {
        // Arrange
        PlantSoort dup = PlantSoort.builder()
                .nederlandseNaam("Tulp")
                .wetenschappelijkeNaam("Tulipa gesneriana")
                .beschrijving("Dubbel")
                .build();

        when(repository.existsByNederlandseNaamIgnoreCase("Tulp")).thenReturn(true);

        // Act + Assert
        DuplicateResourceException ex = assertThrows(
                DuplicateResourceException.class,
                () -> service.create(dup)
        );

        assertTrue(ex.getMessage().contains("PlantSoort bestaat al"));

        verify(repository).existsByNederlandseNaamIgnoreCase("Tulp");
        verify(repository, never()).save(any(PlantSoort.class));
    }
}
