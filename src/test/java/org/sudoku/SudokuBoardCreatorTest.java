package org.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuBoardCreatorTest {
    private SudokuBoardCreator creator;

    @BeforeEach
    void setUp() {
        creator = new SudokuBoardCreator();
    }

    @Test
    void createDeepCopyWhenCopyTheGrid() {
        int[][] original = {
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {4, 5, 6, 7, 8, 9, 1, 2, 3},
                {7, 8, 9, 1, 2, 3, 4, 5, 6},
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {5, 6, 7, 8, 9, 1, 2, 3, 4},
                {8, 9, 1, 2, 3, 4, 5, 6, 7},
                {3, 4, 5, 6, 7, 8, 9, 1, 2},
                {6, 7, 8, 9, 1, 2, 3, 4, 5},
                {9, 1, 2, 3, 4, 5, 6, 7, 8}
        };

        int[][] copy = creator.copyTheGrid(original);

        assertNotSame(original, copy);
        for (int i = 0; i < 9; i++) {
            assertArrayEquals(original[i], copy[i]);
            assertNotSame(original[i], copy[i]);
        }

        copy[0][0] = 99;
        assertEquals(1, original[0][0]);
    }

    @Test
    void returnAllNonZeroPositionsWhenFindPrefilled() {
        int[][] grid = new int[9][9];
        grid[0][0] = 5;
        grid[1][2] = 7;
        grid[8][8] = 9;

        List<int[]> result = creator.findPreFilledCells(grid);

        assertEquals(3, result.size());
        assertArrayEquals(new int[]{0, 0}, result.get(0));
        assertArrayEquals(new int[]{1, 2}, result.get(1));
        assertArrayEquals(new int[]{8, 8}, result.get(2));
    }

    @Test
    void returnEmptyListWhenGridHasNoValuesWhenFindPrefilled() {
        int[][] grid = new int[9][9];

        List<int[]> result = creator.findPreFilledCells(grid);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void removeExactNumberOfDigitsFromFullGrid() {
        int[][] grid = {
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {4, 5, 6, 7, 8, 9, 1, 2, 3},
                {7, 8, 9, 1, 2, 3, 4, 5, 6},
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {5, 6, 7, 8, 9, 1, 2, 3, 4},
                {8, 9, 1, 2, 3, 4, 5, 6, 7},
                {3, 4, 5, 6, 7, 8, 9, 1, 2},
                {6, 7, 8, 9, 1, 2, 3, 4, 5},
                {9, 1, 2, 3, 4, 5, 6, 7, 8}
        };

        creator.removeKDigits(grid, 10);

        int zeroCount = countZeros(grid);
        assertEquals(10, zeroCount);
    }

    @Test
    void removeExactNumberOfDigitsFromFullGridAlready0Digit() {
        int[][] grid = {
                {1, 2, 0, 4, 5, 6, 7, 8, 9},
                {4, 5, 6, 7, 8, 9, 1, 2, 3},
                {7, 8, 9, 1, 2, 3, 4, 5, 6},
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {5, 6, 7, 8, 9, 1, 2, 3, 4},
                {8, 9, 1, 2, 3, 4, 5, 6, 7},
                {3, 4, 5, 6, 7, 8, 9, 1, 2},
                {6, 7, 8, 9, 1, 2, 3, 4, 5},
                {9, 1, 2, 3, 4, 5, 6, 7, 8}
        };

        creator.removeKDigits(grid, 10);

        int zeroCount = countZeros(grid);
        assertEquals(11, zeroCount);
    }

    @Test
    void doNothingWhenKIsZeroWhenRemoveKDigits() {
        int[][] grid = {
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {4, 5, 6, 7, 8, 9, 1, 2, 3},
                {7, 8, 9, 1, 2, 3, 4, 5, 6},
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {5, 6, 7, 8, 9, 1, 2, 3, 4},
                {8, 9, 1, 2, 3, 4, 5, 6, 7},
                {3, 4, 5, 6, 7, 8, 9, 1, 2},
                {6, 7, 8, 9, 1, 2, 3, 4, 5},
                {9, 1, 2, 3, 4, 5, 6, 7, 8}
        };

        int[][] before = creator.copyTheGrid(grid);

        creator.removeKDigits(grid, 0);

        for (int i = 0; i < 9; i++) {
            assertArrayEquals(before[i], grid[i]);
        }
    }

    @Test
    void printGridInExpectedFormat() {
        int[][] grid = new int[9][9];
        grid[0][0] = 5;
        grid[0][1] = 3;
        grid[1][0] = 6;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        try {
            creator.printTheGrid(grid);
        } finally {
            System.setOut(originalOut);
        }

        String output = out.toString();

        assertTrue(output.contains("  1 2 3 4 5 6 7 8 9 "));
        assertTrue(output.contains("A 5 3 _ _ _ _ _ _ _ "));
        assertTrue(output.contains("B 6 _ _ _ _ _ _ _ _ "));
        assertTrue(output.contains("I _ _ _ _ _ _ _ _ _ "));
    }

    @Test
    void fillEmptyGridWithValidSudoku() {
        int[][] grid = new int[9][9];

        boolean result = creator.fillTheGrid(grid);

        assertTrue(result);
        assertTrue(isCompletelyFilled(grid));
        assertTrue(isValidSudoku(grid));
    }

    private int countZeros(int[][] grid) {
        int count = 0;
        for (int[] row : grid) {
            for (int value : row) {
                if (value == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isCompletelyFilled(int[][] grid) {
        for (int[] row : grid) {
            for (int value : row) {
                if (value == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidSudoku(int[][] grid) {
        for (int i = 0; i < 9; i++) {
            boolean[] rowSeen = new boolean[10];
            boolean[] colSeen = new boolean[10];

            for (int j = 0; j < 9; j++) {
                int rowVal = grid[i][j];
                int colVal = grid[j][i];

                if (rowVal < 1 || rowVal > 9 || rowSeen[rowVal]) {
                    return false;
                }
                if (colVal < 1 || colVal > 9 || colSeen[colVal]) {
                    return false;
                }

                rowSeen[rowVal] = true;
                colSeen[colVal] = true;
            }
        }

        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                boolean[] seen = new boolean[10];
                for (int r = boxRow; r < boxRow + 3; r++) {
                    for (int c = boxCol; c < boxCol + 3; c++) {
                        int value = grid[r][c];
                        if (value < 1 || value > 9 || seen[value]) {
                            return false;
                        }
                        seen[value] = true;
                    }
                }
            }
        }

        return true;
    }

}
