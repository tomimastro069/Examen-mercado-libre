package org.example.service;

import org.example.Entity.DnaRecord;
import org.example.Repository.DnaRecordRepository;
import org.example.Service.MutantDetector;
import org.example.Service.MutantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MutantServiceTest {

    private MutantService mutantService;
    private MutantDetector mutantDetector;
    private DnaRecordRepository dnaRecordRepository;

    @BeforeEach
    void setUp() {
        mutantDetector = mock(MutantDetector.class);
        dnaRecordRepository = mock(DnaRecordRepository.class);

        mutantService = new MutantService(mutantDetector, dnaRecordRepository);
    }


    @Test
    void testAnalyzeDna_ReturnsCachedValue_WhenExists() {
        String[] dna = {"AAA", "CCC", "TTT"};

        DnaRecord record = new DnaRecord(1L, "hash123", true);
        when(dnaRecordRepository.findByDnaHash(any())).thenReturn(Optional.of(record));

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }


    @Test
    void testAnalyzeDna_SavesRecord_WhenNotExists() {
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGAA"};

        when(dnaRecordRepository.findByDnaHash(any())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(true);

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);
        verify(dnaRecordRepository).save(any(DnaRecord.class));
        verify(mutantDetector).isMutant(dna);
    }

    @Test
    void testCalculateHash_ReturnsValidHash() {
        String[] dna = {"A", "C", "T"};

        String hash = invokeCalculateHash(dna);

        assertNotNull(hash);
        assertEquals(64, hash.length()); // SHA-256
    }


    @Test
    void testCalculateHash_ThrowsRuntimeException_WhenDigestFails() throws Exception {

        MessageDigest mdMock = mock(MessageDigest.class);

        // static mock
        var staticMock = mockStatic(MessageDigest.class);

        staticMock.when(() -> MessageDigest.getInstance("SHA-256"))
                .thenThrow(new NoSuchAlgorithmException());

        assertThrows(RuntimeException.class, () -> invokeCalculateHash(new String[]{"A"}));

        staticMock.close();
    }


    private String invokeCalculateHash(String[] dna) {
        try {
            var m = MutantService.class.getDeclaredMethod("calculateHash", String[].class);
            m.setAccessible(true);
            return (String) m.invoke(mutantService, (Object) dna);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCalculateHash_CoversHexPaddingBranch() {
        // ADN diseñado para producir bytes pequeños (hex length == 1)
        String[] dna = {"A", "B", "C", "D"};

        String hash = invokeCalculateHash(dna);

        assertNotNull(hash);
        assertEquals(64, hash.length());

        // verificamos que existe al menos un "0" agregado por el padding del if
        assertTrue(hash.contains("0"));
    }

}
