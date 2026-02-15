package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.repository.PlantSoortRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // zet true als je security ook wil testen
class PlantSoortControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantSoortRepository repository;

    private Long plantId;

    @BeforeEach
    void setup() {
        repository.deleteAll();

        PlantSoort plant = PlantSoort.builder()
                .nederlandseNaam("Tulp")
                .wetenschappelijkeNaam("Tulipa gesneriana")
                .familie("Liliaceae")
                .beschrijving("Voorjaarsbloem")
                .bloeiperiodeStart(4)
                .bloeiperiodeEinde(5)
                .giftig(true)
                .inheems(false)
                .onderhoudsniveau("LAAG")
                .slug("tulp")
                .build();

        plantId = repository.save(plant).getId();
    }

    @Test
    @DisplayName("GET /plantsoorten geeft 200 en JSON lijst terug")
    void getAll_returnsJsonList() throws Exception {
        mockMvc.perform(get("/plantsoorten")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nederlandseNaam", is("Tulp")))
                .andExpect(jsonPath("$[0].wetenschappelijkeNaam", is("Tulipa gesneriana")))
                .andExpect(jsonPath("$[0].slug", is("tulp")))
                .andExpect(jsonPath("$[0].giftig", is(true)));
    }

    @Test
    @DisplayName("GET /plantsoorten/{id} geeft 200 en juiste plantsoort")
    void getById_returnsCorrectJson() throws Exception {
        mockMvc.perform(get("/plantsoorten/{id}", plantId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(plantId.intValue())))
                .andExpect(jsonPath("$.nederlandseNaam", is("Tulp")))
                .andExpect(jsonPath("$.familie", is("Liliaceae")))
                .andExpect(jsonPath("$.onderhoudsniveau", is("LAAG")));
    }

    @Test
    @DisplayName("GET /plantsoorten/{id} met onbekend id geeft 4xx error")
    void getById_unknownId_returns4xx() throws Exception {
        mockMvc.perform(get("/plantsoorten/{id}", 999999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
