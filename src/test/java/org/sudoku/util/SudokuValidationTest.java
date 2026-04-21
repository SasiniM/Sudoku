package org.sudoku.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SudokuValidationTest {

    @Test
    void returnTrueWhenNumberFitsRowColumnAndSubgrid() {
        int[][] board = new int[9][9];
        board[0][1] = 3;
        board[1][0] = 4;
        board[1][1] = 5;

        boolean result = SudokuValidation.isValid(board, 0, 0, 1);

        assertTrue(result);
    }

    @Test
    void returnFalseWhenNumberAlreadyExistsInRow() {
        int[][] board = new int[9][9];
        board[0][4] = 7;

        boolean result = SudokuValidation.isValid(board, 0, 0, 7);

        assertFalse(result);
    }

    @Test
    void returnFalseWhenNumberAlreadyExistsInColumn() {
        int[][] board = new int[9][9];
        board[5][0] = 8;

        boolean result = SudokuValidation.isValid(board, 0, 0, 8);

        assertFalse(result);
    }

    @Test
    void returnFalseWhenNumberAlreadyExistsInSubgrid() {
        int[][] board = new int[9][9];
        board[1][1] = 9;

        boolean result = SudokuValidation.isValid(board, 0, 0, 9);

        assertFalse(result);
    }

    @Test
    void returnNullWhenEntireGridIsValid() {
        int[][] grid = {
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

        String result = SudokuValidation.validateEntireGrid(grid);

        assertEquals("No rule violations detected.", result);
    }

    @Test
    void returnRowErrorWhenRowHasDuplicate() {
        int[][] grid = new int[9][9];
        grid[0][0] = 5;
        grid[0][3] = 5;

        String result = SudokuValidation.validateEntireGrid(grid);

        assertEquals("Number 5 already exists in Row A.", result);
    }

    @Test
    void returnColumnErrorWhenColumnHasDuplicate() {
        int[][] grid = new int[9][9];
        grid[0][0] = 6;
        grid[4][0] = 6;

        String result = SudokuValidation.validateEntireGrid(grid);

        assertEquals("Number 6 already exists in Column 1.", result);
    }

    @Test
    void returnSubgridErrorWhenSubgridHasDuplicate() {
        int[][] grid = new int[9][9];
        grid[0][0] = 4;
        grid[1][1] = 4;

        String result = SudokuValidation.validateEntireGrid(grid);

        assertEquals("Number 4 already exists in the 3x3 subgrid.", result);
    }

    @Test
    void returnTrueForValidReferences() {
        assertTrue(SudokuValidation.isValidCellReference("A1"));
        assertTrue(SudokuValidation.isValidCellReference("C5"));
    }

    @Test
    void returnFalseForInvalidReferences() {
        assertFalse(SudokuValidation.isValidCellReference("a1"));
        assertFalse(SudokuValidation.isValidCellReference("J1"));
        assertFalse(SudokuValidation.isValidCellReference("A0"));
        assertFalse(SudokuValidation.isValidCellReference("A10"));
        assertFalse(SudokuValidation.isValidCellReference("1A"));
        assertFalse(SudokuValidation.isValidCellReference(""));
    }

    @Test
    void returnTrueWhenCellExistsInPredefinedList() {
        List<int[]> predefined = new ArrayList<>();
        predefined.add(new int[]{0, 0});
        predefined.add(new int[]{3, 4});

        boolean result = SudokuValidation.isPredefinedCell(3, 4, predefined);

        assertTrue(result);
    }

    @Test
    void returnFalseWhenCellDoesNotExistInPredefinedList() {
        List<int[]> predefined = new ArrayList<>();
        predefined.add(new int[]{0, 0});
        predefined.add(new int[]{3, 4});

        boolean result = SudokuValidation.isPredefinedCell(2, 2, predefined);

        assertFalse(result);
    }

    @Test
    void returnTrueWhenGridMatchesSolution() {
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

        int[][] solution = {
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

        boolean result = SudokuValidation.isPuzzleComplete(grid, solution);

        assertTrue(result);
    }

    @Test
    void returnFalseWhenGridDoesNotMatchSolution() {
        int[][] grid = {
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {4, 5, 6, 7, 8, 9, 1, 2, 3},
                {7, 8, 9, 1, 2, 3, 4, 5, 6},
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {5, 6, 7, 8, 9, 1, 2, 3, 4},
                {8, 9, 1, 2, 3, 4, 5, 6, 7},
                {3, 4, 5, 6, 7, 8, 9, 1, 2},
                {6, 7, 8, 9, 1, 2, 3, 4, 5},
                {9, 1, 2, 3, 4, 5, 6, 7, 7}
        };

        int[][] solution = {
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

        boolean result = SudokuValidation.isPuzzleComplete(grid, solution);

        assertFalse(result);
    }

    @Test
    void returnTrueWhenValueNotInSubgrid() {
        int[][] board = new int[9][9];
        board[0][0] = 1;
        board[0][1] = 2;
        board[1][0] = 3;

        boolean result = SudokuValidation.subgridConstraint(board, 1, 1, 9);

        assertTrue(result);
    }

    @Test
    void returnFalseWhenValueAlreadyInSubgrid() {
        int[][] board = new int[9][9];
        board[2][2] = 8;

        boolean result = SudokuValidation.subgridConstraint(board, 0, 0, 8);

        assertFalse(result);
    }

}
