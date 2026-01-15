package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Foto;
import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.FotoRepository;
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

@ExtendWith(MockitoExtension.class)
class FotoServiceTest {

    @Mock
    private FotoRepository fotoRepository;

    @Mock
    private PlantSoortRepository plantSoortRepository;

    @InjectMocks
    private FotoService service;

    @DisplayName("getByPlantSoort() retourneert alle foto's voor een plantsoort")
    @Test
    void getByPlantSoort_returnsList() {
        // Arrange
        Foto f1 = Foto.builder().id(1L).url("https://example.com/1.jpg").build();
        Foto f2 = Foto.builder().id(2L).url("https://example.com/2.jpg").build();

        when(fotoRepository.findByPlantSoortId(10L)).thenReturn(List.of(f1, f2));

        // Act
        List<Foto> result = service.getByPlantSoort(10L);

        // Assert
        assertEquals(2, result.size());
        assertEquals("https://example.com/1.jpg", result.get(0).getUrl());
        assertEquals("https://example.com/2.jpg", result.get(1).getUrl());

        verify(fotoRepository).findByPlantSoortId(10L);
    }

    @DisplayName("addToPlantSoort() koppelt foto aan plant en slaat op wanneer plant bestaat")
    @Test
    void addToPlantSoort_savesFoto_whenPlantExists() {
        // Arrange
        PlantSoort plant = PlantSoort.builder()
                .id(10L)
                .nederlandseNaam("Lavendel")
                .wetenschappelijkeNaam("Lavandula angustifolia")
                .build();

        when(plantSoortRepository.findById(10L)).thenReturn(Optional.of(plant));

        Foto input = Foto.builder()
                .url("https://example.com/lavendel.jpg")
                .hoofdfoto(true)
                .build();

        Foto saved = Foto.builder()
                .id(99L)
                .url("https://example.com/lavendel.jpg")
                .hoofdfoto(true)
                .plantSoort(plant)
                .build();

        when(fotoRepository.save(input)).thenReturn(saved);

        // Act
        Foto result = service.addToPlantSoort(10L, input);

        // Assert
        assertNotNull(result);
        assertEquals(99L, result.getId());
        assertEquals("https://example.com/lavendel.jpg", result.getUrl());

        // belangrijk: service zet plantSoort op de foto vóór save()
        assertNotNull(input.getPlantSoort(), "Foto moet gekoppeld zijn aan PlantSoort vóór save()");
        assertEquals(10L, input.getPlantSoort().getId());

        verify(plantSoortRepository).findById(10L);
        verify(fotoRepository).save(input);
    }

    @DisplayName("addToPlantSoort() gooit ResourceNotFoundException wanneer plant niet bestaat")
    @Test
    void addToPlantSoort_throwsResourceNotFound_whenPlantMissing() {
        // Arrange
        when(plantSoortRepository.findById(404L)).thenReturn(Optional.empty());

        Foto input = Foto.builder()
                .url("https://example.com/404.jpg")
                .build();

        // Act + Assert
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.addToPlantSoort(404L, input)
        );

        assertTrue(ex.getMessage().contains("PlantSoort niet gevonden"));

        verify(plantSoortRepository).findById(404L);
        verify(fotoRepository, never()).save(any(Foto.class));
    }
}
