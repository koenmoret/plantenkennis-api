package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Foto;
import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.FotoRepository;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
        verifyNoMoreInteractions(fotoRepository, plantSoortRepository);
    }

    @DisplayName("addToPlantSoort() koppelt foto aan plant en slaat op wanneer plant bestaat (geen bestaande hoofdfoto)")
    @Test
    void addToPlantSoort_savesFoto_whenPlantExists_noExistingHoofdfoto() {
        // Arrange
        PlantSoort plant = PlantSoort.builder()
                .id(10L)
                .nederlandseNaam("Lavendel")
                .wetenschappelijkeNaam("Lavandula angustifolia")
                .build();

        when(plantSoortRepository.findById(10L)).thenReturn(Optional.of(plant));

        // Service-logica: bij hoofdfoto=true wordt bestaande hoofdfoto opgevraagd
        when(fotoRepository.findFirstByPlantSoortIdAndHoofdfotoTrue(10L))
                .thenReturn(Optional.empty());

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

        // Niet te strikt stubben op "input", want service wijzigt input (plantSoort zetten)
        when(fotoRepository.save(any(Foto.class))).thenReturn(saved);

        // Act
        Foto result = service.addToPlantSoort(10L, input);

        // Assert
        assertNotNull(result);
        assertEquals(99L, result.getId());
        assertEquals("https://example.com/lavendel.jpg", result.getUrl());
        assertTrue(result.isHoofdfoto());

        // Captor: check dat de foto die naar save ging gekoppeld is aan plant
        ArgumentCaptor<Foto> captor = ArgumentCaptor.forClass(Foto.class);
        verify(fotoRepository).save(captor.capture());

        Foto savedArg = captor.getValue();
        assertNotNull(savedArg.getPlantSoort(), "Foto moet gekoppeld zijn aan PlantSoort vóór save()");
        assertEquals(10L, savedArg.getPlantSoort().getId());
        assertTrue(savedArg.isHoofdfoto());

        verify(plantSoortRepository).findById(10L);
        verify(fotoRepository).findFirstByPlantSoortIdAndHoofdfotoTrue(10L);
        verifyNoMoreInteractions(fotoRepository, plantSoortRepository);
    }

    @DisplayName("addToPlantSoort() zet bestaande hoofdfoto uit wanneer nieuwe hoofdfoto wordt toegevoegd")
    @Test
    void addToPlantSoort_unsetsExistingHoofdfoto_whenNewHoofdfotoTrue() {
        // Arrange
        Long plantId = 10L;

        PlantSoort plant = PlantSoort.builder().id(plantId).build();
        when(plantSoortRepository.findById(plantId)).thenReturn(Optional.of(plant));

        Foto bestaande = Foto.builder()
                .id(1L)
                .url("https://example.com/old.jpg")
                .hoofdfoto(true)
                .plantSoort(plant)
                .build();

        when(fotoRepository.findFirstByPlantSoortIdAndHoofdfotoTrue(plantId))
                .thenReturn(Optional.of(bestaande));

        // save() echo: return exact argument to simplify
        when(fotoRepository.save(any(Foto.class))).thenAnswer(inv -> inv.getArgument(0));

        Foto input = Foto.builder()
                .url("https://example.com/new.jpg")
                .hoofdfoto(true)
                .build();

        // Act
        Foto result = service.addToPlantSoort(plantId, input);

        // Assert
        assertNotNull(result);
        assertEquals("https://example.com/new.jpg", result.getUrl());
        assertTrue(result.isHoofdfoto());
        assertEquals(plant, result.getPlantSoort());

        // bestaande hoofdfoto moet uitgezet worden
        assertFalse(bestaande.isHoofdfoto(), "Bestaande hoofdfoto moet uitgezet worden");

        // Verwacht: 2 saves (1x bestaande op false, 1x nieuwe foto)
        verify(fotoRepository, times(2)).save(any(Foto.class));
        verify(fotoRepository).findFirstByPlantSoortIdAndHoofdfotoTrue(plantId);
        verify(plantSoortRepository).findById(plantId);

        verifyNoMoreInteractions(fotoRepository, plantSoortRepository);
    }

    @DisplayName("addToPlantSoort() slaat foto op zonder hoofdfoto-logica wanneer hoofdfoto=false")
    @Test
    void addToPlantSoort_savesFoto_whenHoofdfotoFalse_doesNotQueryExistingHoofdfoto() {
        // Arrange
        Long plantId = 10L;

        PlantSoort plant = PlantSoort.builder().id(plantId).build();
        when(plantSoortRepository.findById(plantId)).thenReturn(Optional.of(plant));

        Foto input = Foto.builder()
                .url("https://example.com/normal.jpg")
                .hoofdfoto(false)
                .build();

        Foto saved = Foto.builder()
                .id(77L)
                .url("https://example.com/normal.jpg")
                .hoofdfoto(false)
                .plantSoort(plant)
                .build();

        when(fotoRepository.save(any(Foto.class))).thenReturn(saved);

        // Act
        Foto result = service.addToPlantSoort(plantId, input);

        // Assert
        assertNotNull(result);
        assertEquals(77L, result.getId());
        assertFalse(result.isHoofdfoto());
        assertEquals(plant, result.getPlantSoort());

        verify(plantSoortRepository).findById(plantId);
        verify(fotoRepository).save(any(Foto.class));
        verify(fotoRepository, never()).findFirstByPlantSoortIdAndHoofdfotoTrue(anyLong());

        verifyNoMoreInteractions(fotoRepository, plantSoortRepository);
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
        verify(fotoRepository, never()).findFirstByPlantSoortIdAndHoofdfotoTrue(anyLong());
        verify(fotoRepository, never()).save(any(Foto.class));

        verifyNoMoreInteractions(fotoRepository, plantSoortRepository);
    }
}
