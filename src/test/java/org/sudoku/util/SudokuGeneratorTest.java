package org.sudoku.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SudokuGeneratorTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void copyTheGrid_shouldCreateDeepCopy() {
        int[][] original = new int[9][9];
        original[0][0] = 5;
        original[1][1] = 7;

        int[][] copy = SudokuGenerator.copyTheGrid(original);

        assertNotSame(original, copy);
        assertNotSame(original[0], copy[0]);
        assertEquals(5, copy[0][0]);
        assertEquals(7, copy[1][1]);

        copy[0][0] = 9;
        assertEquals(5, original[0][0]);
    }

    @Test
    void removeKDigits_shouldRemoveExactNumberOfDigits() {
        int[][] grid = createFilledGrid();

        int nonZeroBefore = countNonZero(grid);

        SudokuGenerator.removeKDigits(grid, 10);

        int nonZeroAfter = countNonZero(grid);

        assertEquals(nonZeroBefore - 10, nonZeroAfter);
    }

    @Test
    void removeKDigits_shouldDoNothingWhenKIsZero() {
        int[][] grid = createFilledGrid();
        int[][] before = SudokuGenerator.copyTheGrid(grid);

        SudokuGenerator.removeKDigits(grid, 0);

        assertArrayEquals(before, grid);
    }

    @Test
    void removeKDigits_shouldRemoveExactNumberOfDigits_WithZeroValueCell() {
        int[][] grid = createFilledGrid();

        grid[0][0] = 0;

        int nonZeroBefore = countNonZero(grid);

        SudokuGenerator.removeKDigits(grid, 10);

        int nonZeroAfter = countNonZero(grid);

        assertEquals(nonZeroBefore - 10, nonZeroAfter);
    }

    @Test
    void findPreFilledCells_shouldReturnAllNonZeroCells() {
        int[][] grid = new int[9][9];
        grid[0][0] = 5;
        grid[0][1] = 3;
        grid[2][4] = 7;

        List<int[]> preFilledCells = SudokuGenerator.findPreFilledCells(grid);

        assertEquals(3, preFilledCells.size());
        assertTrue(containsCell(preFilledCells, 0, 0));
        assertTrue(containsCell(preFilledCells, 0, 1));
        assertTrue(containsCell(preFilledCells, 2, 4));
    }

    @Test
    void findPreFilledCells_shouldReturnEmptyListWhenGridHasNoValues() {
        int[][] grid = new int[9][9];

        List<int[]> preFilledCells = SudokuGenerator.findPreFilledCells(grid);

        assertNotNull(preFilledCells);
        assertTrue(preFilledCells.isEmpty());
    }

    @Test
    void printTheGrid_shouldPrintTitleAndGridContent() {
        int[][] grid = new int[9][9];
        grid[0][0] = 5;
        grid[0][1] = 3;
        grid[1][1] = 7;

        SudokuGenerator.printTheGrid(grid, "Here is your puzzle:");

        String output = outputStream.toString();

        assertTrue(output.contains("Here is your puzzle:"));
        assertTrue(output.contains("1 2 3 4 5 6 7 8 9"));
        assertTrue(output.contains("A 5 3"));
        assertTrue(output.contains("B _ 7"));
        assertTrue(output.contains("_"));
    }

    @Test
    void fillTheGrid_shouldFillAllCells() {
        int[][] grid = new int[9][9];

        boolean result = SudokuGenerator.fillTheGrid(grid);

        assertTrue(result);
        assertEquals(81, countNonZero(grid));
    }

    @Test
    void fillTheGrid_shouldProduceValidSudoku() {
        int[][] grid = new int[9][9];

        boolean result = SudokuGenerator.fillTheGrid(grid);

        assertTrue(result);
        assertTrue(isValidSudoku(grid));
    }

    @Test
    void fillTheGrid_shouldReturnTrueForAlreadyFilledGrid() {
        int[][] grid = createSolvedGrid();

        boolean result = SudokuGenerator.fillTheGrid(grid);

        assertTrue(result);
        assertTrue(isValidSudoku(grid));
    }
    private boolean containsCell(List<int[]> cells, int row, int col) {
        return cells.stream().anyMatch(cell -> cell[0] == row && cell[1] == col);
    }

    private int countNonZero(int[][] grid) {
        int count = 0;
        for (int[] row : grid) {
            for (int value : row) {
                if (value != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isValidSudoku(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10];
            for (int col = 0; col < 9; col++) {
                int value = grid[row][col];
                if (value < 1 || value > 9 || seen[value]) {
                    return false;
                }
                seen[value] = true;
            }
        }

        for (int col = 0; col < 9; col++) {
            boolean[] seen = new boolean[10];
            for (int row = 0; row < 9; row++) {
                int value = grid[row][col];
                if (value < 1 || value > 9 || seen[value]) {
                    return false;
                }
                seen[value] = true;
            }
        }

        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                boolean[] seen = new boolean[10];
                for (int row = boxRow; row < boxRow + 3; row++) {
                    for (int col = boxCol; col < boxCol + 3; col++) {
                        int value = grid[row][col];
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

    private int[][] createFilledGrid() {
        return createSolvedGrid();
    }

    private int[][] createSolvedGrid() {
        return new int[][]{
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
    }
}
