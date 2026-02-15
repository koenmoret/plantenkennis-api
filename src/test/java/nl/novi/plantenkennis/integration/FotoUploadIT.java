package nl.novi.plantenkennis.integration;

import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.repository.FotoRepository;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FotoUploadIT {

    @Autowired MockMvc mockMvc;
    @Autowired PlantSoortRepository plantSoortRepository;
    @Autowired FotoRepository fotoRepository;

    @Test
    void uploadFoto_slaatBestandpadEnMetadataOp() throws Exception {
        // Arrange: maak plantsoort (minimaal verplichte velden!)
        PlantSoort plant = new PlantSoort();
        plant.setNederlandseNaam("Testplant");
        plant.setWetenschappelijkeNaam("Testus plantus");

        PlantSoort savedPlant = plantSoortRepository.save(plant);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                new byte[]{1, 2, 3, 4}
        );

        // Act + Assert (201)
        mockMvc.perform(
                        multipart("/plantsoorten/{id}/fotos", savedPlant.getId())
                                .file(file)
                                .param("altTekst", "Test afbeelding")
                                .param("hoofdfoto", "true")
                                .with(jwt().authorities(() -> "ROLE_client_admin"))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.storagePath").isString())
                .andExpect(jsonPath("$.contentType").value("image/png"))
                .andExpect(jsonPath("$.hoofdfoto").value(true));

        // Check DB: er is 1 foto gekoppeld aan plantsoort
        var fotos = fotoRepository.findByPlantSoortId(savedPlant.getId());
        assertThat(fotos).hasSize(1);

        var foto = fotos.getFirst();
        assertThat(foto.getStoragePath()).isNotBlank();
        assertThat(foto.getUrl()).isNotBlank();
        assertThat(foto.getContentType()).isEqualTo("image/png");
        assertThat(foto.isHoofdfoto()).isTrue();
    }
}
