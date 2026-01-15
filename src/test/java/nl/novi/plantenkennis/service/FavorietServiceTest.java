package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Favoriet;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.repository.FavorietRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavorietServiceTest {

    @Mock
    private FavorietRepository repository;

    @InjectMocks
    private FavorietService service;

    @DisplayName("create() slaat favoriet op wanneer deze nog niet bestaat")
    @Test
    void create_savesFavoriet_whenNotDuplicate() {
        // Arrange
        Favoriet input = Favoriet.builder()
                .gebruikerId(1L)
                .plantSoortId(2L)
                .build();

        when(repository.existsByGebruikerIdAndPlantSoortId(1L, 2L)).thenReturn(false);

        Favoriet saved = Favoriet.builder()
                .id(10L)
                .gebruikerId(1L)
                .plantSoortId(2L)
                .build();

        when(repository.save(input)).thenReturn(saved);

        // Act
        Favoriet result = service.create(input);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(1L, result.getGebruikerId());
        assertEquals(2L, result.getPlantSoortId());

        verify(repository).existsByGebruikerIdAndPlantSoortId(1L, 2L);
        verify(repository).save(input);
    }

    @DisplayName("create() gooit DuplicateResourceException wanneer favoriet al bestaat")
    @Test
    void create_throwsDuplicate_whenDuplicate() {
        // Arrange
        Favoriet input = Favoriet.builder()
                .gebruikerId(1L)
                .plantSoortId(2L)
                .build();

        when(repository.existsByGebruikerIdAndPlantSoortId(1L, 2L)).thenReturn(true);

        // Act + Assert
        DuplicateResourceException ex = assertThrows(
                DuplicateResourceException.class,
                () -> service.create(input)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("favoriet bestaat al"));

        verify(repository).existsByGebruikerIdAndPlantSoortId(1L, 2L);
        verify(repository, never()).save(any(Favoriet.class));
    }

    @DisplayName("getByGebruikerId() geeft lijst van favorieten terug")
    @Test
    void getByGebruikerId_returnsList() {
        // Arrange
        Favoriet f1 = Favoriet.builder().id(1L).gebruikerId(1L).plantSoortId(10L).build();
        Favoriet f2 = Favoriet.builder().id(2L).gebruikerId(1L).plantSoortId(11L).build();

        when(repository.findByGebruikerId(1L)).thenReturn(List.of(f1, f2));

        // Act
        List<Favoriet> result = service.getByGebruikerId(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals(10L, result.get(0).getPlantSoortId());
        assertEquals(11L, result.get(1).getPlantSoortId());

        verify(repository).findByGebruikerId(1L);
    }
}
