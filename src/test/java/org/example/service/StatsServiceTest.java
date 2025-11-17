package org.example.service;

import org.example.Repository.DnaRecordRepository;
import org.example.Service.StatsService;
import org.example.dto.StatsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatsServiceTest {

    private StatsService statsService;
    private DnaRecordRepository dnaRecordRepository;

    @BeforeEach
    void setUp() {
        dnaRecordRepository = mock(DnaRecordRepository.class);
        statsService = new StatsService();
        statsService.dnaRecordRepository = dnaRecordRepository;
    }

    @Test
    void testGetStats_CalculatesCorrectRatio() {
        when(dnaRecordRepository.countMutants()).thenReturn(40L);
        when(dnaRecordRepository.countHumans()).thenReturn(100L);

        StatsResponse response = statsService.getStats();

        assertEquals(40, response.getCountMutantDna());
        assertEquals(100, response.getCountHumanDna());
        assertEquals(0.4, response.getRatio());
    }

    @Test
    void testGetStats_RatioZero_WhenHumansZero() {
        when(dnaRecordRepository.countMutants()).thenReturn(10L);
        when(dnaRecordRepository.countHumans()).thenReturn(0L);

        StatsResponse response = statsService.getStats();

        assertEquals(0.0, response.getRatio());
    }
}
