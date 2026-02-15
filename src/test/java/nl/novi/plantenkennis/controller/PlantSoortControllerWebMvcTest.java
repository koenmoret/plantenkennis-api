package nl.novi.plantenkennis.controller;

import nl.novi.plantenkennis.entity.PlantSoort;
import nl.novi.plantenkennis.service.PlantKenmerkService;
import nl.novi.plantenkennis.service.PlantSoortService;
import nl.novi.plantenkennis.service.SynoniemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Alleen weblaag (controller + mvc), geen echte DB/service
@WebMvcTest(PlantSoortController.class)
@AutoConfigureMockMvc(addFilters = false) // handig als je Spring Security aan hebt staan
@Import(nl.novi.plantenkennis.exception.GlobalExceptionHandler.class) // zorgt dat exceptions netjes JSON worden
class PlantSoortControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlantSoortService plantSoortService;

    @MockBean
    private PlantKenmerkService plantKenmerkService;

    @MockBean
    private SynoniemService synoniemService;

    @Test
    void getById_returns200_andJsonBody() throws Exception {
        // Arrange
        PlantSoort tulp = PlantSoort.builder()
                .id(1L)
                .nederlandseNaam("Tulp")
                .wetenschappelijkeNaam("Tulipa gesneriana")
                .familie("Liliaceae")
                .beschrijving("Voorjaarsbol")
                .bloeiperiodeStart(4)
                .bloeiperiodeEinde(5)
                .giftig(true)
                .inheems(false)
                .onderhoudsniveau("LAAG")
                .slug("tulp")
                .updatedAt(LocalDateTime.now())
                .build();

        when(plantSoortService.getById(1L)).thenReturn(tulp);

        // Act + Assert
        mockMvc.perform(get("/plantsoorten/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nederlandseNaam").value("Tulp"))
                .andExpect(jsonPath("$.slug").value("tulp"));
    }
}
