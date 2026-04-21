package org.sudoku.util;

import org.sudoku.model.Command;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

public class SudokuValidation {

    /**
     *
     * Checking the validity of the number which is going to insert into the grid
     * According to all three constraints (row, column and 3x3 subgrid)
     */
    public static boolean isValid(int[][] board, int row, int column, int value) {
        return (rowConstraint(board, row, value)
                && columnConstraint(board, column, value)
                && subgridConstraint(board, row, column, value));
    }

    /**
     * Validate the entire current grid when check.
     * Returns null if valid, otherwise returns first found error.
     */
    public static String validateEntireGrid(int[][] grid) {
        // Check rows
        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10];
            for (int col = 0; col < 9; col++) {
                int value = grid[row][col];
                if (value != 0) {
                    if (seen[value]) {
                        return MessageFormat.format("Number {0} already exists in Row {1}.", value, (char)('A' + row));
                    }
                    seen[value] = true;
                }
            }
        }

        // Check columns
        for (int col = 0; col < 9; col++) {
            boolean[] seen = new boolean[10];
            for (int row = 0; row < 9; row++) {
                int value = grid[row][col];
                if (value != 0) {
                    if (seen[value]) {
                        return MessageFormat.format("Number {0} already exists in Column {1}.", value, col + 1);
                    }
                    seen[value] = true;
                }
            }
        }

        // Check 3x3 subgrids
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                boolean[] seen = new boolean[10];
                for (int row = boxRow; row < boxRow + 3; row++) {
                    for (int col = boxCol; col < boxCol + 3; col++) {
                        int value = grid[row][col];
                        if (value != 0) {
                            if (seen[value]) {
                                return MessageFormat.format("Number {0} already exists in the 3x3 subgrid.", value);
                            }
                            seen[value] = true;
                        }
                    }
                }
            }
        }

        return "No rule violations detected.";
    }

    /**
     * Check the validness of the cell reference coming as a player input
     */
    public static boolean isValidCellReference(String cellRef) {
        return cellRef.matches("^[A-I][1-9]$");
    }

    /**
     * Player cannot change or clear any of the system pre-filled cell values
     * Check whether the player input cell reference is a pre-filled or not
     */
    public static boolean isPredefinedCell(int row, int col, List<int[]> predefined) {
        for (int[] cell : predefined) {
            if (cell[0] == row && cell[1] == col) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPredefinedCell(Command command, List<int[]> predefined) {
        for (int[] cell : predefined) {
            if (cell[0] == command.getRow() && cell[1] == command.getColumn()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check the completion of the grid correctly
     */
    public static boolean isPuzzleComplete(int[][] grid, int[][] solution) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] != solution[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check the validity of  value the only in the 3x3 subgrid
     */
    private static boolean rowConstraint(int[][] board, int row, int value) {
        for (int column = 0; column < 9; column++) {
            if (board[row][column] == value) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check the validity of  value th only in column
     */
    private static boolean columnConstraint(int[][] board, int column, int value) {
        for (int row = 0; row < 9; row++) {
            if (board[row][column] == value) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check the validity of  value th only in row
     */
    public static boolean subgridConstraint(int[][] board, int row, int column, int value) {
        int subgridRowStart = (row / 3) * 3;
        int subgridRowEnd = subgridRowStart + 3;

        int subgridColumnStart = (column / 3) * 3;
        int subgridColumnEnd = subgridColumnStart + 3;

        for (int r = subgridRowStart; r < subgridRowEnd; r++) {
            for (int c = subgridColumnStart; c < subgridColumnEnd; c++) {
                if (board[r][c] == value)
                    return false;
            }
        }
        return true;
    }
}
