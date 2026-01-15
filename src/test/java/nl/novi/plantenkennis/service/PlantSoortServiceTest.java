package nl.novi.plantenkennis.service;

import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.junit.jupiter.api.DisplayName;
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

/**
 * Unit tests voor {@link PlantSoortService}.
 *
 * Deze tests controleren de businesslogica van de service,
 * waarbij de repository wordt gemockt zodat er geen database
 * wordt aangesproken.
 */
@ExtendWith(MockitoExtension.class)
class PlantSoortServiceTest {

    /**
     * Mock van de repository.
     * Hiermee simuleren we databasegedrag zonder echte database.
     */
    @Mock
    private PlantSoortRepository repository;

    /**
     * De service die we testen.
     * Mockito injecteert automatisch de gemockte repository.
     */
    @InjectMocks
    private PlantSoortService service;

    /**
     * Test het happy flow scenario van getAll().
     *
     * Verwachting:
     * - De repository retourneert twee PlantSoort objecten
     * - De service geeft deze lijst ongewijzigd terug
     */
    @DisplayName("Geeft alle plantsoorten terug wanneer deze bestaan")
    @Test
    void getAll_happyFlow_returnsListOfPlantSoorten() {
        // Arrange: maak voorbeelddata aan
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

        // Simuleer repositorygedrag
        when(repository.findAll()).thenReturn(List.of(tulp, roos));

        // Act: roep de service-methode aan
        List<PlantSoort> result = service.getAll();

        // Assert: controleer resultaat
        assertEquals(2, result.size());
        assertEquals("Tulp", result.get(0).getNaam());
        assertEquals("Roos", result.get(1).getNaam());

        // Verifieer dat de repository daadwerkelijk is aangeroepen
        verify(repository).findAll();
    }

    /**
     * Test het happy flow scenario van getById().
     *
     * Verwachting:
     * - De repository retourneert een PlantSoort
     * - De service geeft dit object correct terug
     */
    @DisplayName("Geeft één plantsoort terug op basis van id")
    @Test
    void getById_happyFlow_returnsPlantSoort() {
        // Arrange: maak een voorbeeld PlantSoort aan
        PlantSoort tulp = PlantSoort.builder()
                .id(1L)
                .naam("Tulp")
                .latijnseNaam("Tulipa")
                .omschrijving("Voorbeeld plantsoort")
                .build();

        // Simuleer repositorygedrag
        when(repository.findById(1L)).thenReturn(Optional.of(tulp));

        // Act: roep de service-methode aan
        PlantSoort result = service.getById(1L);

        // Assert: controleer of het object correct is teruggegeven
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Tulp", result.getNaam());

        // Verifieer correcte repository-aanroep
        verify(repository).findById(1L);
    }
}