package org.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SudokuSolverTest {
    private SudokuSolver solver;

    @BeforeEach
    void setUp() {
        solver = new SudokuSolver();
    }

    @Test
    void clearCellAndReturnMessageWhenCellIsNotPreFilled() {
        int[][] grid = new int[9][9];
        grid[0][0] = 5;

        List<int[]> preFilledCells = new ArrayList<>();

        String result = solver.handleClear(grid, preFilledCells, 0, 0);

        assertEquals(0, grid[0][0]);
        assertEquals("A1 cleared.", result);
    }

    @Test
    void returnInvalidMessageWhenCellIsPreFilled() {
        int[][] grid = new int[9][9];
        grid[1][2] = 7;

        List<int[]> preFilledCells = new ArrayList<>();
        preFilledCells.add(new int[]{1, 2});

        String result = solver.handleClear(grid, preFilledCells, 1, 2);

        assertEquals("B3 cleared.", result);
        assertEquals(0, grid[1][2]);
    }

    @Test
    void buildCorrectCellReference() {
        int[][] grid = new int[9][9];
        grid[8][8] = 9;

        List<int[]> preFilledCells = new ArrayList<>();

        String result = solver.handleClear(grid, preFilledCells, 8, 8);

        assertEquals("I9 cleared.", result);
        assertEquals(0, grid[8][8]);
    }

    @Test
    void returnNoHintMessageWhenPuzzleIsComplete() {
        int[][] gridPuzzle = {
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

        int[][] copiedGrid = {
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

        String result = solver.giveHint(gridPuzzle, copiedGrid);

        assertEquals("No hint available. Puzzle may already be complete.", result);
    }

    @Test
    void returnHintForOnlyEmptyCell() {
        int[][] gridPuzzle = {
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

        int[][] copiedGrid = {
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

        String result = solver.giveHint(gridPuzzle, copiedGrid);

        assertEquals("Hint: Cell A3 = 3", result);
    }

    @Test
    void returnHintFormatWhenMultipleEmptyCellsExist() {
        int[][] gridPuzzle = new int[9][9];
        int[][] copiedGrid = new int[9][9];

        gridPuzzle[0][0] = 1;
        copiedGrid[0][1] = 2;
        copiedGrid[1][1] = 5;

        String result = solver.giveHint(gridPuzzle, copiedGrid);

        assertTrue(result.startsWith("Hint: Cell "));
        assertTrue(result.contains(" = "));
    }
}
