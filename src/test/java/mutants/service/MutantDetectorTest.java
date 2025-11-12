package mutants.service;
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
    void testInvalidDna_NullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(null);
        });
    }

    @Test
    void testInvalidDna_EmptyArray() {
        String[] dna = {};
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
    }

    @Test
    void testInvalidDna_NotSquareMatrix() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT"
        };
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
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
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
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
}