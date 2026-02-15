package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.exception.ResourceNotFoundException;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantSoortServiceTest {

    @Mock
    private PlantSoortRepository repository;

    @InjectMocks
    private PlantSoortService service;

    // -------------------------
    // getAll()
    // -------------------------

    @DisplayName("getAll() geeft alle plantsoorten terug")
    @Test
    void getAll_returnsList() {
        // Arrange
        PlantSoort tulp = PlantSoort.builder().id(1L).nederlandseNaam("Tulp").build();
        PlantSoort roos = PlantSoort.builder().id(2L).nederlandseNaam("Roos").build();
        when(repository.findAll()).thenReturn(List.of(tulp, roos));

        // Act
        List<PlantSoort> result = service.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Tulp", result.get(0).getNederlandseNaam());
        verify(repository).findAll();
    }

    // -------------------------
    // getById()
    // -------------------------

    @DisplayName("getById() geeft plantsoort terug wanneer deze bestaat")
    @Test
    void getById_happyFlow() {
        // Arrange
        PlantSoort tulp = PlantSoort.builder().id(1L).nederlandseNaam("Tulp").build();
        when(repository.findById(1L)).thenReturn(Optional.of(tulp));

        // Act
        PlantSoort result = service.getById(1L);

        // Assert
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
        verifyNoMoreInteractions(repository);
    }

    // -------------------------
    // create()
    // -------------------------

    @DisplayName("create() slaat plantsoort op wanneer naam geldig is en nog niet bestaat")
    @Test
    void create_happyFlow_savesEntity() {
        // Arrange
        PlantSoort nieuw = PlantSoort.builder()
                .nederlandseNaam("Lavendel")
                .wetenschappelijkeNaam("Lavandula angustifolia")
                .build();

        when(repository.existsByNederlandseNaamIgnoreCase("Lavendel")).thenReturn(false);

        PlantSoort saved = PlantSoort.builder()
                .id(10L)
                .nederlandseNaam("Lavendel")
                .wetenschappelijkeNaam("Lavandula angustifolia")
                .build();

        when(repository.save(any(PlantSoort.class))).thenReturn(saved);

        // Act
        PlantSoort result = service.create(nieuw);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Lavendel", result.getNederlandseNaam());

        verify(repository).existsByNederlandseNaamIgnoreCase("Lavendel");
        verify(repository).save(nieuw);
    }

    @DisplayName("create() gooit IllegalArgumentException wanneer nederlandseNaam null is")
    @Test
    void create_nullName_throwsIllegalArgumentException() {
        // Arrange
        PlantSoort nieuw = PlantSoort.builder().nederlandseNaam(null).build();

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(nieuw)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("verplicht"));

        verify(repository, never()).existsByNederlandseNaamIgnoreCase(anyString());
        verify(repository, never()).save(any());
    }

    @DisplayName("create() gooit IllegalArgumentException wanneer nederlandseNaam leeg/whitespace is")
    @Test
    void create_blankName_throwsIllegalArgumentException() {
        // Arrange
        PlantSoort nieuw = PlantSoort.builder().nederlandseNaam("   ").build();

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(nieuw)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("verplicht"));

        verify(repository, never()).existsByNederlandseNaamIgnoreCase(anyString());
        verify(repository, never()).save(any());
    }

    @DisplayName("create() gooit IllegalArgumentException wanneer naam al bestaat")
    @Test
    void create_duplicate_throwsIllegalArgumentException() {
        // Arrange
        PlantSoort dup = PlantSoort.builder().nederlandseNaam("Tulp").build();
        when(repository.existsByNederlandseNaamIgnoreCase("Tulp")).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(dup)
        );
        assertTrue(ex.getMessage().contains("bestaat al"));

        verify(repository).existsByNederlandseNaamIgnoreCase("Tulp");
        verify(repository, never()).save(any());
    }

    // -------------------------
    // searchTableFields()
    // (deze tests raken ook private helpers)
    // -------------------------

    @DisplayName("searchTableFields() normaliseert strings en zet onderhoudsniveau naar uppercase; null/blank datums -> null")
    @Test
    void searchTableFields_normalizesInputs_andNullDates() {
        // Arrange
        when(repository.searchTableFields(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(Page.empty());

        Pageable pageable = Pageable.unpaged();

        // Act
        Page<PlantSoort> result = service.searchTableFields(
                "  tulp  ",          // q -> "tulp"
                "  liliaceae ",      // familie -> "liliaceae"
                true,                // giftig
                false,               // inheems
                "  laag  ",          // onderhoudsniveau -> "LAAG"
                "  tulp  ",          // slug -> "tulp"
                4,                   // bloeimaand
                "   ",               // updatedAfter -> null
                null,                // updatedBefore -> null
                pageable
        );

        // Assert
        assertNotNull(result);

        // Captor om exact te checken wat er naar repository gaat
        ArgumentCaptor<String> qCap = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> famCap = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> onCap = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> slugCap = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<LocalDateTime> afterCap = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> beforeCap = ArgumentCaptor.forClass(LocalDateTime.class);

        verify(repository).searchTableFields(
                qCap.capture(),
                famCap.capture(),
                eq(true),
                eq(false),
                onCap.capture(),
                slugCap.capture(),
                eq(4),
                afterCap.capture(),
                beforeCap.capture(),
                eq(pageable)
        );

        assertEquals("tulp", qCap.getValue());
        assertEquals("liliaceae", famCap.getValue());
        assertEquals("LAAG", onCap.getValue());
        assertEquals("tulp", slugCap.getValue());
        assertNull(afterCap.getValue());
        assertNull(beforeCap.getValue());
    }

    @DisplayName("searchTableFields() parsed updatedAfter/updatedBefore ISO-8601 correct")
    @Test
    void searchTableFields_parsesDateTimes() {
        // Arrange
        when(repository.searchTableFields(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(Page.empty());

        Pageable pageable = Pageable.unpaged();

        String afterStr = "2026-01-01T10:15:30";
        String beforeStr = "2026-02-01T12:00:00";

        // Act
        service.searchTableFields(
                null, null, null, null,
                "gemiddeld",   // -> "GEMIDDELD"
                null, null,
                afterStr,
                beforeStr,
                pageable
        );

        // Assert
        ArgumentCaptor<LocalDateTime> afterCap = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> beforeCap = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<String> onCap = ArgumentCaptor.forClass(String.class);

        verify(repository).searchTableFields(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                onCap.capture(),
                isNull(),
                isNull(),
                afterCap.capture(),
                beforeCap.capture(),
                eq(pageable)
        );

        assertEquals("GEMIDDELD", onCap.getValue());
        assertEquals(LocalDateTime.parse(afterStr), afterCap.getValue());
        assertEquals(LocalDateTime.parse(beforeStr), beforeCap.getValue());
    }
}
