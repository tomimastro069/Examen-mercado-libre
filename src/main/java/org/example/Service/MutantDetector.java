package org.example.Service;

import org.springframework.stereotype.Component;

@Component
public class MutantDetector {
    public boolean isMutant(String[] dna) {
        if (!isValidDna(dna)) {
            throw new IllegalArgumentException("Invalid DNA sequence");
        }

        // Convertir String[] a char[][]
        char[][] dnaMatrix = convertToMatrix(dna);
        int n = dnaMatrix.length;
        int sequencesFound = 0;

        // Buscar secuencias horizontales y verticales
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n - 4; j++) {
                // Horizontal
                if (hasSequence(dnaMatrix[i][j], dnaMatrix[i][j + 1],
                        dnaMatrix[i][j + 2], dnaMatrix[i][j + 3])) {
                    sequencesFound++;
                    if (sequencesFound > 1) return true;
                }

                // Vertical
                if (hasSequence(dnaMatrix[j][i], dnaMatrix[j + 1][i],
                        dnaMatrix[j + 2][i], dnaMatrix[j + 3][i])) {
                    sequencesFound++;
                    if (sequencesFound > 1) return true;
                }
            }
        }

        // Buscar diagonales descendentes (\)
        for (int i = 0; i <= n - 4; i++) {
            for (int j = 0; j <= n - 4; j++) {
                if (hasSequence(dnaMatrix[i][j], dnaMatrix[i + 1][j + 1],
                        dnaMatrix[i + 2][j + 2], dnaMatrix[i + 3][j + 3])) {
                    sequencesFound++;
                    if (sequencesFound > 1) return true;
                }
            }
        }

        // Buscar diagonales ascendentes (/)
        for (int i = 0; i <= n - 4; i++) {
            for (int j = 3; j < n; j++) {
                if (hasSequence(dnaMatrix[i][j], dnaMatrix[i + 1][j - 1],
                        dnaMatrix[i + 2][j - 2], dnaMatrix[i + 3][j - 3])) {
                    sequencesFound++;
                    if (sequencesFound > 1) return true;
                }
            }
        }

        return false;
    }

    private char[][] convertToMatrix(String[] dna) {
        int n = dna.length;
        char[][] matrix = new char[n][n];

        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        return matrix;
    }

    private boolean hasSequence(char c1, char c2, char c3, char c4) {
        return c1 == c2 && c2 == c3 && c3 == c4;
    }

    private boolean isValidDna(String[] dna) {
        if (dna == null || dna.length == 0) {
            return false;
        }

        int n = dna.length;

        for (String row : dna) {
            if (row == null || row.length() != n) {
                return false;
            }

            for (char c : row.toCharArray()) {
                if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                    return false;
                }
            }
        }

        return true;
    }
}