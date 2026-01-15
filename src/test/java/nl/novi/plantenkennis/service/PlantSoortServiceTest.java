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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests voor {@link PlantSoortService}.
 *
 * Deze tests controleren de businesslogica van de service,
 * waarbij de repository wordt gemockt zodat er geen database
 * wordt aangesproken.
 */
@ExtendWith(MockitoExtension.class)
class PlantSoortServiceTest {

    @Mock
    private PlantSoortRepository repository;

    @InjectMocks
    private PlantSoortService service;

    @DisplayName("Geeft alle plantsoorten terug wanneer deze bestaan")
    @Test
    void getAll_happyFlow_returnsListOfPlantSoorten() {
        // Arrange
        PlantSoort tulp = PlantSoort.builder()
                .id(1L)
                .naam("Tulp")
                .latijnseNaam("Tulipa")
                .omschrijving("Voorbeeld plantsoort")
                .build();

        PlantSoort roos = PlantSoort.builder()
                .id(2L)
                .naam("Roos")
                .latijnseNaam("Rosa")
                .omschrijving("Nog een voorbeeld")
                .build();

        when(repository.findAll()).thenReturn(List.of(tulp, roos));

        // Act
        List<PlantSoort> result = service.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Tulp", result.get(0).getNaam());
        assertEquals("Roos", result.get(1).getNaam());
        verify(repository).findAll();
    }

    @DisplayName("Geeft één plantsoort terug op basis van id wanneer deze bestaat")
    @Test
    void getById_happyFlow_returnsPlantSoort() {
        // Arrange
        PlantSoort tulp = PlantSoort.builder()
                .id(1L)
                .naam("Tulp")
                .latijnseNaam("Tulipa")
                .omschrijving("Voorbeeld plantsoort")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(tulp));

        // Act
        PlantSoort result = service.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Tulp", result.getNaam());
        verify(repository).findById(1L);
    }

    @DisplayName("Gooi ResourceNotFoundException wanneer plantsoort niet bestaat")
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

    @DisplayName("Slaat plantsoort op wanneer naam nog niet bestaat")
    @Test
    void create_happyFlow_savesPlantSoort() {
        // Arrange
        PlantSoort nieuw = PlantSoort.builder()
                .naam("Lavendel")
                .latijnseNaam("Lavandula")
                .omschrijving("Paarse plant")
                .build();

        when(repository.existsByNaamIgnoreCase("Lavendel")).thenReturn(false);

        PlantSoort saved = PlantSoort.builder()
                .id(10L)
                .naam("Lavendel")
                .latijnseNaam("Lavandula")
                .omschrijving("Paarse plant")
                .build();

        when(repository.save(nieuw)).thenReturn(saved);

        // Act
        PlantSoort result = service.create(nieuw);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Lavendel", result.getNaam());

        verify(repository).existsByNaamIgnoreCase("Lavendel");
        verify(repository).save(nieuw);
    }

    @DisplayName("Gooi DuplicateResourceException wanneer plantsoortnaam al bestaat")
    @Test
    void create_duplicate_throwsDuplicateResourceException() {
        // Arrange
        PlantSoort dup = PlantSoort.builder()
                .naam("Tulp")
                .latijnseNaam("Tulipa")
                .omschrijving("Dubbel")
                .build();

        when(repository.existsByNaamIgnoreCase("Tulp")).thenReturn(true);

        // Act + Assert
        DuplicateResourceException ex = assertThrows(
                DuplicateResourceException.class,
                () -> service.create(dup)
        );

        assertTrue(ex.getMessage().contains("PlantSoort bestaat al"));

        verify(repository).existsByNaamIgnoreCase("Tulp");
        verify(repository, never()).save(any(PlantSoort.class));
    }
}
