package org.example.service;

import org.example.Service.MutantDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    // -----------------------------------------
    //  TESTS VALIDOS - MUTANTE / NO MUTANTE
    // -----------------------------------------

    @Test
    void testMutantCase() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testNonMutantCase() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testHorizontalSequences() {
        String[] dna = {
                "AAAATG",
                "TGCAGT",
                "GCTTTT",
                "CGATCG",
                "ATCGAT",
                "TGCATG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testVerticalSequences() {
        String[] dna = {
                "ATGCGA",
                "ATGTGC",
                "ATATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testDiagonalSequences() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testOnlyOneSequence() {
        String[] dna = {
                "AAAATG",
                "TGCAGT",
                "GCATGT",
                "CGATCG",
                "ATCGAT",
                "TGCATG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testSmall4x4Matrix_Mutant() {
        String[] dna = {
                "AAAA",
                "CCCC",
                "TAGT",
                "GGTC"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    void testSmall4x4Matrix_NonMutant() {
        String[] dna = {
                "AAAT",
                "CCCC",
                "TAGT",
                "GGTC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // -----------------------------------------
    //  TESTS DE VALIDACIÓN DE ADN
    // -----------------------------------------

    @Test
    void testInvalidDna_NullArray() {
        assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(null));
    }

    @Test
    void testInvalidDna_EmptyArray() {
        String[] dna = {};
        assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(dna));
    }

    @Test
    void testInvalidDna_NotSquareMatrix() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT"
        };
        assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(dna));
    }

    @Test
    void testInvalidDna_InvalidCharacters() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGXAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(dna));
    }

    // >>> FALTANTE PARA 100% DE COBERTURA <<<
    @Test
    void testInvalidDna_RowIsNull() {
        String[] dna = {
                "ATGC",
                null,
                "ATGC",
                "ATGC"
        };
        assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(dna));
    }

    // -----------------------------------------
    //  RAMAS INTERNAS FALTANTES PARA 100%
    // -----------------------------------------

    @Test
    void testValidDna_NoMutant_FullMatrixTraversal() {
        String[] dna = {
                "ATCG",
                "TAGC",
                "CGAT",
                "GCAT"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testDiagonalAscending_NoMutation() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "GTTTGT",
                "AGACGG",
                "CCACTA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    void testDiagonalDoubleSequence_EarlyReturn() {
        String[] dna = {
                "AAAAAA",
                "CAAAAC",
                "TCAACT",
                "AGAAAG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }
    @Test
    void testIsMutant_WhenMoreThanOneSequenceFound() {
        String[] dna = {
                "AAAA",
                "CCCC",
                "TTTT",
                "GGGG"
        };

        MutantDetector detector = new MutantDetector();

        boolean result = detector.isMutant(dna);

        assertTrue(result); // debe entrar en sequencesFound > 1
    }
}
