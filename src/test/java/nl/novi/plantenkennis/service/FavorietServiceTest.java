package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Favoriet;
import nl.novi.plantenkennis.entity.Gebruiker;
import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.repository.FavorietRepository;
import nl.novi.plantenkennis.repository.GebruikerRepository;
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
class FavorietServiceTest {

    @Mock
    private FavorietRepository favorietRepository;

    @Mock
    private GebruikerRepository gebruikerRepository;

    @Mock
    private PlantSoortRepository plantSoortRepository;

    @InjectMocks
    private FavorietService service;

    @DisplayName("getByGebruikerId() geeft lijst van favorieten terug")
    @Test
    void getByGebruikerId_returnsList() {
        // Arrange
        Long gebruikerId = 1L;

        Gebruiker gebruiker = new Gebruiker();
        // (id setten is vaak private; als je geen setter hebt, dan is dit genoeg voor deze test
        // omdat we alleen list size checken en repository-call verifiëren)

        Favoriet f1 = Favoriet.builder().id(1L).gebruiker(gebruiker).plantSoort(new PlantSoort()).build();
        Favoriet f2 = Favoriet.builder().id(2L).gebruiker(gebruiker).plantSoort(new PlantSoort()).build();

        when(favorietRepository.findByGebruiker_Id(gebruikerId)).thenReturn(List.of(f1, f2));

        // Act
        List<Favoriet> result = service.getByGebruikerId(gebruikerId);

        // Assert
        assertEquals(2, result.size());
        verify(favorietRepository).findByGebruiker_Id(gebruikerId);
    }

    @DisplayName("create() slaat favoriet op wanneer deze nog niet bestaat")
    @Test
    void create_savesFavoriet_whenNotDuplicate() {
        // Arrange
        Long gebruikerId = 1L;
        Long plantSoortId = 2L;

        when(favorietRepository.existsByGebruiker_IdAndPlantSoort_Id(gebruikerId, plantSoortId))
                .thenReturn(false);

        Gebruiker gebruiker = new Gebruiker();
        PlantSoort plantSoort = new PlantSoort();

        when(gebruikerRepository.findById(gebruikerId)).thenReturn(Optional.of(gebruiker));
        when(plantSoortRepository.findById(plantSoortId)).thenReturn(Optional.of(plantSoort));

        Favoriet saved = Favoriet.builder()
                .id(10L)
                .gebruiker(gebruiker)
                .plantSoort(plantSoort)
                .build();

        when(favorietRepository.save(any(Favoriet.class))).thenReturn(saved);

        // Act
        Favoriet result = service.create(gebruikerId, plantSoortId);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());

        verify(favorietRepository).existsByGebruiker_IdAndPlantSoort_Id(gebruikerId, plantSoortId);
        verify(gebruikerRepository).findById(gebruikerId);
        verify(plantSoortRepository).findById(plantSoortId);

        // Belangrijk: we saven een Favoriet met relaties, niet met losse ids
        verify(favorietRepository).save(argThat(f ->
                f.getGebruiker() == gebruiker && f.getPlantSoort() == plantSoort
        ));
    }

    @DisplayName("create() gooit DuplicateResourceException wanneer favoriet al bestaat")
    @Test
    void create_throwsDuplicate_whenDuplicate() {
        // Arrange
        Long gebruikerId = 1L;
        Long plantSoortId = 2L;

        when(favorietRepository.existsByGebruiker_IdAndPlantSoort_Id(gebruikerId, plantSoortId))
                .thenReturn(true);

        // Act + Assert
        DuplicateResourceException ex = assertThrows(
                DuplicateResourceException.class,
                () -> service.create(gebruikerId, plantSoortId)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("favoriet bestaat al"));

        verify(favorietRepository).existsByGebruiker_IdAndPlantSoort_Id(gebruikerId, plantSoortId);
        verify(gebruikerRepository, never()).findById(anyLong());
        verify(plantSoortRepository, never()).findById(anyLong());
        verify(favorietRepository, never()).save(any());
    }

    @DisplayName("delete() verwijdert favoriet wanneer deze bestaat")
    @Test
    void delete_deletesFavoriet_whenExists() {
        // Arrange
        Long gebruikerId = 1L;
        Long plantSoortId = 2L;

        Favoriet favoriet = Favoriet.builder()
                .id(99L)
                .gebruiker(new Gebruiker())
                .plantSoort(new PlantSoort())
                .build();

        when(favorietRepository.findByGebruiker_IdAndPlantSoort_Id(gebruikerId, plantSoortId))
                .thenReturn(Optional.of(favoriet));

        // Act
        service.delete(gebruikerId, plantSoortId);

        // Assert
        verify(favorietRepository).findByGebruiker_IdAndPlantSoort_Id(gebruikerId, plantSoortId);
        verify(favorietRepository).delete(favoriet);
    }

    @DisplayName("delete() gooit RuntimeException wanneer favoriet niet bestaat")
    @Test
    void delete_throws_whenNotFound() {
        // Arrange
        Long gebruikerId = 1L;
        Long plantSoortId = 2L;

        when(favorietRepository.findByGebruiker_IdAndPlantSoort_Id(gebruikerId, plantSoortId))
                .thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.delete(gebruikerId, plantSoortId)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("favoriet niet gevonden"));

        verify(favorietRepository).findByGebruiker_IdAndPlantSoort_Id(gebruikerId, plantSoortId);
        verify(favorietRepository, never()).delete(any());
    }
}
