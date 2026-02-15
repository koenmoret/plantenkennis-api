package nl.novi.plantenkennis.repository;

import nl.novi.plantenkennis.entity.PlantSoort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class PlantSoortRepositoryIT {

    @Autowired
    private PlantSoortRepository repository;

    @Test
    @DisplayName("Repository slaat entity correct op en haalt deze terug uit database")
    void saveAndFindById_integrationTest() {
        // Arrange
        PlantSoort plant = PlantSoort.builder()
                .nederlandseNaam("Tulp")
                .wetenschappelijkeNaam("Tulipa gesneriana")
                .familie("Liliaceae")
                .beschrijving("Voorjaarsbloem")
                .giftig(true)
                .inheems(false)
                .onderhoudsniveau("LAAG")
                .slug("tulp")
                .build();

        // Act
        PlantSoort saved = repository.save(plant);
        PlantSoort found = repository.findById(saved.getId()).orElseThrow();

        // Assert
        assertEquals("Tulp", found.getNederlandseNaam());
        assertEquals("tulp", found.getSlug());
        assertTrue(found.isGiftig());
    }

    @Test
    @DisplayName("existsByNederlandseNaamIgnoreCase werkt case-insensitive")
    void existsByNederlandseNaamIgnoreCase_integrationTest() {
        // Arrange
        repository.save(
                PlantSoort.builder()
                        .nederlandseNaam("Lavendel")
                        .wetenschappelijkeNaam("Lavandula angustifolia")
                        .slug("lavendel")
                        .build()
        );

        // Act + Assert
        assertTrue(repository.existsByNederlandseNaamIgnoreCase("lavendel"));
        assertTrue(repository.existsByNederlandseNaamIgnoreCase("LAVENDEL"));
    }
}
