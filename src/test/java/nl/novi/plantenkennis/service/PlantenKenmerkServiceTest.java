package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Kenmerk;
import nl.novi.plantenkennis.entity.PlantKenmerk;
import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.KenmerkRepository;
import nl.novi.plantenkennis.repository.PlantKenmerkRepository;
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
class PlantKenmerkServiceTest {

    @Mock
    private PlantKenmerkRepository plantKenmerkRepository;

    @Mock
    private PlantSoortRepository plantSoortRepository;

    @Mock
    private KenmerkRepository kenmerkRepository;

    @InjectMocks
    private PlantKenmerkService service;

    @DisplayName("getByPlantSoort(): retourneert koppelingen wanneer plant bestaat")
    @Test
    void getByPlantSoort_returnsList_whenPlantExists() {
        // Arrange
        Long plantId = 10L;

        PlantSoort plant = PlantSoort.builder().id(plantId).build();
        Kenmerk k1 = Kenmerk.builder().id(1L).type("kleur").waarde("paars").build();
        Kenmerk k2 = Kenmerk.builder().id(2L).type("blad").waarde("smal").build();

        PlantKenmerk pk1 = PlantKenmerk.builder().id(100L).plantSoort(plant).kenmerk(k1).build();
        PlantKenmerk pk2 = PlantKenmerk.builder().id(101L).plantSoort(plant).kenmerk(k2).build();

        when(plantSoortRepository.existsById(plantId)).thenReturn(true);
        when(plantKenmerkRepository.findByPlantSoortId(plantId)).thenReturn(List.of(pk1, pk2));

        // Act
        List<PlantKenmerk> result = service.getByPlantSoort(plantId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(100L, result.get(0).getId());
        assertEquals(101L, result.get(1).getId());

        verify(plantSoortRepository).existsById(plantId);
        verify(plantKenmerkRepository).findByPlantSoortId(plantId);
        verifyNoMoreInteractions(plantKenmerkRepository, plantSoortRepository, kenmerkRepository);
    }

    @DisplayName("getByPlantSoort(): gooit ResourceNotFoundException wanneer plant niet bestaat")
    @Test
    void getByPlantSoort_throws_whenPlantMissing() {
        // Arrange
        Long plantId = 404L;
        when(plantSoortRepository.existsById(plantId)).thenReturn(false);

        // Act + Assert
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getByPlantSoort(plantId)
        );

        assertTrue(ex.getMessage().contains("PlantSoort niet gevonden"));

        verify(plantSoortRepository).existsById(plantId);
        verify(plantKenmerkRepository, never()).findByPlantSoortId(anyLong());
        verifyNoMoreInteractions(plantKenmerkRepository, plantSoortRepository, kenmerkRepository);
    }

    @DisplayName("addKenmerkToPlant(): slaat koppeling op wanneer plant en kenmerk bestaan en koppeling nog niet bestaat")
    @Test
    void addKenmerkToPlant_saves_whenNotExists() {
        // Arrange
        Long plantId = 10L;
        Long kenmerkId = 5L;

        PlantSoort plant = PlantSoort.builder().id(plantId).build();
        Kenmerk kenmerk = Kenmerk.builder().id(kenmerkId).type("kleur").waarde("paars").build();

        when(plantSoortRepository.findById(plantId)).thenReturn(Optional.of(plant));
        when(kenmerkRepository.findById(kenmerkId)).thenReturn(Optional.of(kenmerk));
        when(plantKenmerkRepository.existsByPlantSoortIdAndKenmerkId(plantId, kenmerkId)).thenReturn(false);

        PlantKenmerk saved = PlantKenmerk.builder()
                .id(999L)
                .plantSoort(plant)
                .kenmerk(kenmerk)
                .build();

        when(plantKenmerkRepository.save(any(PlantKenmerk.class))).thenReturn(saved);

        // Act
        PlantKenmerk result = service.addKenmerkToPlant(plantId, kenmerkId);

        // Assert
        assertNotNull(result);
        assertEquals(999L, result.getId());
        assertEquals(plant, result.getPlantSoort());
        assertEquals(kenmerk, result.getKenmerk());

        verify(plantSoortRepository).findById(plantId);
        verify(kenmerkRepository).findById(kenmerkId);
        verify(plantKenmerkRepository).existsByPlantSoortIdAndKenmerkId(plantId, kenmerkId);
        verify(plantKenmerkRepository).save(any(PlantKenmerk.class));
        verifyNoMoreInteractions(plantKenmerkRepository, plantSoortRepository, kenmerkRepository);
    }

    @DisplayName("addKenmerkToPlant(): gooit DuplicateResourceException wanneer koppeling al bestaat")
    @Test
    void addKenmerkToPlant_throwsDuplicate_whenAlreadyExists() {
        // Arrange
        Long plantId = 10L;
        Long kenmerkId = 5L;

        PlantSoort plant = PlantSoort.builder().id(plantId).build();
        Kenmerk kenmerk = Kenmerk.builder().id(kenmerkId).build();

        when(plantSoortRepository.findById(plantId)).thenReturn(Optional.of(plant));
        when(kenmerkRepository.findById(kenmerkId)).thenReturn(Optional.of(kenmerk));
        when(plantKenmerkRepository.existsByPlantSoortIdAndKenmerkId(plantId, kenmerkId)).thenReturn(true);

        // Act + Assert
        DuplicateResourceException ex = assertThrows(
                DuplicateResourceException.class,
                () -> service.addKenmerkToPlant(plantId, kenmerkId)
        );

        assertTrue(ex.getMessage().contains("Koppeling bestaat al"));

        verify(plantSoortRepository).findById(plantId);
        verify(kenmerkRepository).findById(kenmerkId);
        verify(plantKenmerkRepository).existsByPlantSoortIdAndKenmerkId(plantId, kenmerkId);
        verify(plantKenmerkRepository, never()).save(any());
        verifyNoMoreInteractions(plantKenmerkRepository, plantSoortRepository, kenmerkRepository);
    }

    @DisplayName("addKenmerkToPlant(): gooit ResourceNotFoundException wanneer plant niet bestaat")
    @Test
    void addKenmerkToPlant_throws_whenPlantMissing() {
        // Arrange
        Long plantId = 404L;
        Long kenmerkId = 5L;

        when(plantSoortRepository.findById(plantId)).thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.addKenmerkToPlant(plantId, kenmerkId)
        );

        assertTrue(ex.getMessage().contains("PlantSoort niet gevonden"));

        verify(plantSoortRepository).findById(plantId);
        verifyNoMoreInteractions(plantKenmerkRepository, plantSoortRepository, kenmerkRepository);
    }

    @DisplayName("addKenmerkToPlant(): gooit ResourceNotFoundException wanneer kenmerk niet bestaat")
    @Test
    void addKenmerkToPlant_throws_whenKenmerkMissing() {
        // Arrange
        Long plantId = 10L;
        Long kenmerkId = 404L;

        PlantSoort plant = PlantSoort.builder().id(plantId).build();

        when(plantSoortRepository.findById(plantId)).thenReturn(Optional.of(plant));
        when(kenmerkRepository.findById(kenmerkId)).thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.addKenmerkToPlant(plantId, kenmerkId)
        );

        assertTrue(ex.getMessage().contains("Kenmerk niet gevonden"));

        verify(plantSoortRepository).findById(plantId);
        verify(kenmerkRepository).findById(kenmerkId);
        verifyNoMoreInteractions(plantKenmerkRepository, plantSoortRepository, kenmerkRepository);
    }

    @DisplayName("removeKenmerkFromPlant(): verwijdert koppeling wanneer deze bestaat")
    @Test
    void removeKenmerkFromPlant_deletes_whenExists() {
        // Arrange
        Long plantId = 10L;
        Long kenmerkId = 5L;

        PlantSoort plant = PlantSoort.builder().id(plantId).build();
        Kenmerk kenmerk = Kenmerk.builder().id(kenmerkId).build();
        PlantKenmerk link = PlantKenmerk.builder().id(123L).plantSoort(plant).kenmerk(kenmerk).build();

        when(plantKenmerkRepository.findByPlantSoortIdAndKenmerkId(plantId, kenmerkId))
                .thenReturn(Optional.of(link));

        // Act
        service.removeKenmerkFromPlant(plantId, kenmerkId);

        // Assert
        verify(plantKenmerkRepository).findByPlantSoortIdAndKenmerkId(plantId, kenmerkId);
        verify(plantKenmerkRepository).delete(link);
        verifyNoMoreInteractions(plantKenmerkRepository, plantSoortRepository, kenmerkRepository);
    }

    @DisplayName("removeKenmerkFromPlant(): gooit ResourceNotFoundException wanneer koppeling niet bestaat")
    @Test
    void removeKenmerkFromPlant_throws_whenLinkMissing() {
        // Arrange
        Long plantId = 10L;
        Long kenmerkId = 404L;

        when(plantKenmerkRepository.findByPlantSoortIdAndKenmerkId(plantId, kenmerkId))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.removeKenmerkFromPlant(plantId, kenmerkId)
        );

        assertTrue(ex.getMessage().contains("Koppeling niet gevonden"));

        verify(plantKenmerkRepository).findByPlantSoortIdAndKenmerkId(plantId, kenmerkId);
        verify(plantKenmerkRepository, never()).delete(any());
        verifyNoMoreInteractions(plantKenmerkRepository, plantSoortRepository, kenmerkRepository);
    }
}
