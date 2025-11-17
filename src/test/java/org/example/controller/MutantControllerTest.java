package org.example.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Controller.MutantController;
import org.example.Service.MutantService;
import org.example.Service.StatsService;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    void testCheckMutant_Returns200_WhenMutant() throws Exception {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        DnaRequest request = new DnaRequest(dna);

        when(mutantService.analyzeDna(any())).thenReturn(true);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testCheckMutant_Returns403_WhenNotMutant() throws Exception {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };

        DnaRequest request = new DnaRequest(dna);

        when(mutantService.analyzeDna(any())).thenReturn(false);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCheckMutant_Returns400_WhenInvalidDna() throws Exception {
        String[] dna = {"INVALID"};

        DnaRequest request = new DnaRequest(dna);

        when(mutantService.analyzeDna(any()))
                .thenThrow(new IllegalArgumentException("ADN inválido"));

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStats_ReturnsCorrectStats() throws Exception {

        StatsResponse stats = new StatsResponse(40, 100, 0.4);
        when(statsService.getStats()).thenReturn(stats);

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['cantidad de humanos']").value(100))
                .andExpect(jsonPath("$['cantidad de mutantes:']").value(40))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }
}
