package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.Kenmerk;
import nl.novi.plantenkennis.exception.DuplicateResourceException;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.KenmerkRepository;
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
class KenmerkServiceTest {

    @Mock
    private KenmerkRepository repository;

    @InjectMocks
    private KenmerkService service;

    @DisplayName("getAll() geeft alle kenmerken terug")
    @Test
    void getAll_returnsList() {
        // Arrange
        Kenmerk k1 = Kenmerk.builder().id(1L).type("bloemkleur").waarde("paars").build();
        Kenmerk k2 = Kenmerk.builder().id(2L).type("bladtype").waarde("ovaal").build();
        when(repository.findAll()).thenReturn(List.of(k1, k2));

        // Act
        List<Kenmerk> result = service.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("bloemkleur", result.get(0).getType());
        assertEquals("bladtype", result.get(1).getType());
        verify(repository).findAll();
    }

    @DisplayName("getById() retourneert kenmerk wanneer gevonden")
    @Test
    void getById_returnsKenmerk_whenFound() {
        // Arrange
        Kenmerk kenmerk = Kenmerk.builder().id(10L).type("bloemkleur").waarde("geel").build();
        when(repository.findById(10L)).thenReturn(Optional.of(kenmerk));

        // Act
        Kenmerk result = service.getById(10L);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("bloemkleur", result.getType());
        assertEquals("geel", result.getWaarde());
        verify(repository).findById(10L);
    }

    @DisplayName("getById() gooit ResourceNotFoundException wanneer kenmerk niet bestaat")
    @Test
    void getById_throwsResourceNotFound_whenMissing() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getById(99L)
        );

        assertTrue(ex.getMessage().contains("Kenmerk niet gevonden met id: 99"));
        verify(repository).findById(99L);
    }

    @DisplayName("create() slaat kenmerk op wanneer het niet dubbel is")
    @Test
    void create_savesKenmerk_whenNotDuplicate() {
        // Arrange
        Kenmerk input = Kenmerk.builder().type("bloemkleur").waarde("rood").build();

        when(repository.existsByTypeIgnoreCaseAndWaardeIgnoreCase("bloemkleur", "rood"))
                .thenReturn(false);

        Kenmerk saved = Kenmerk.builder().id(1L).type("bloemkleur").waarde("rood").build();
        when(repository.save(input)).thenReturn(saved);

        // Act
        Kenmerk result = service.create(input);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("bloemkleur", result.getType());
        assertEquals("rood", result.getWaarde());

        verify(repository).existsByTypeIgnoreCaseAndWaardeIgnoreCase("bloemkleur", "rood");
        verify(repository).save(input);
    }

    @DisplayName("create() gooit DuplicateResourceException wanneer type+waarde al bestaat")
    @Test
    void create_throwsDuplicateResourceException_whenDuplicate() {
        // Arrange
        Kenmerk input = Kenmerk.builder().type("bloemkleur").waarde("paars").build();

        when(repository.existsByTypeIgnoreCaseAndWaardeIgnoreCase("bloemkleur", "paars"))
                .thenReturn(true);

        // Act + Assert
        DuplicateResourceException ex = assertThrows(
                DuplicateResourceException.class,
                () -> service.create(input)
        );

        assertTrue(ex.getMessage().contains("Kenmerk bestaat al"));
        assertTrue(ex.getMessage().contains("type='bloemkleur'"));
        assertTrue(ex.getMessage().contains("waarde='paars'"));

        verify(repository).existsByTypeIgnoreCaseAndWaardeIgnoreCase("bloemkleur", "paars");
        verify(repository, never()).save(any(Kenmerk.class));
    }
}
