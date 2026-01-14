package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantSoortServiceTest {

    @Mock
    private PlantSoortRepository repository;

    @InjectMocks
    private PlantSoortService service;

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

}
