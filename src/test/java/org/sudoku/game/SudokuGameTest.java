package org.sudoku.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class SudokuGameTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void startGame_shouldReturnFalse_whenPuzzleIsCompleted() {
        SudokuGame game = Mockito.spy(new SudokuGame());

        int[][] solvedGrid = createSolvedGrid();
        int[][] grid = copyGrid(solvedGrid);

        // one missing value
        grid[0][1] = 0;

        // insert correct value, then extra newline for "Press any key to play again..."
        Scanner scanner = new Scanner("A2 3\nquit\n");

        String input = "A2 3\nquit\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Mockito.doReturn(false).when(game).gameLoop(any(Scanner.class), any(int[][].class), any(int[][].class));

        game.startGame();

        verify(game).gameLoop(any(Scanner.class), any(int[][].class), any(int[][].class));
    }

    @Test
    void gameLoop_shouldReturnTrue_whenQuitCommandIsEntered() {
        SudokuGame game = new SudokuGame();

        int[][] grid = createPuzzleGrid();
        int[][] copiedOriginal = copyGrid(grid);

        Scanner scanner = new Scanner("quit\n");

        boolean result = game.gameLoop(scanner, grid, copiedOriginal);

        scanner.close();
        assertFalse(result);
        assertTrue(output().contains("Thanks for playing Sudoku!"));
    }

    @Test
    void gameLoop_shouldInsertNumberIntoEditableCell() {
        SudokuGame game = new SudokuGame();

        int[][] grid = createPuzzleGrid();
        int[][] copiedOriginal = copyGrid(grid);

        // A2 is empty and editable
        Scanner scanner = new Scanner("A2 5\nquit\n");

        boolean result = game.gameLoop(scanner, grid, copiedOriginal);

        assertFalse(result);
        assertEquals(5, grid[0][1]);
        assertTrue(output().contains("Move accepted."));
        assertTrue(output().contains("Current grid:"));
    }

    @Test
    void gameLoop_shouldClearNumberFromEditableCell() {
        SudokuGame game = new SudokuGame();

        int[][] grid = createPuzzleGrid();
        int[][] copiedOriginal = copyGrid(grid);

        copiedOriginal[5][5] = 1;

        Scanner scanner = new Scanner("A2 4\nA2 clear\nquit\n");

        boolean result = game.gameLoop(scanner, grid, copiedOriginal);

        assertFalse(result);
        assertEquals(0, grid[0][1]);
        assertTrue(output().contains("A2 cleared."));
    }

    @Test
    void gameLoop_shouldRejectInsertIntoPrefilledCell() {
        SudokuGame game = new SudokuGame();

        int[][] grid = createPuzzleGrid();
        int[][] copiedOriginal = copyGrid(grid);

        // A1 is pre-filled with 5
        Scanner scanner = new Scanner("A1 9\nquit\n");

        boolean result = game.gameLoop(scanner, grid, copiedOriginal);

        assertFalse(result);
        assertEquals(5, grid[0][0]); // unchanged
        assertTrue(output().contains("Invalid"));
        assertTrue(output().contains("A1 is pre-filled."));
    }

    @Test
    void gameLoop_shouldPrintHint_whenHintCommandEntered() {
        SudokuGame game = new SudokuGame();

        int[][] solvedGrid = createSolvedGrid();
        int[][] grid = copyGrid(solvedGrid);

        // only one empty cell -> deterministic hint
        grid[0][1] = 0;

        Scanner scanner = new Scanner("hint\nquit\n");

        boolean result = game.gameLoop(scanner, grid, solvedGrid);

        assertFalse(result);
        assertTrue(output().contains("Hint:"));
        assertTrue(output().contains("= 3")); // solved value at A2
    }

    @Test
    void gameLoop_shouldPrintNoHintAvailable_whenPuzzleAlreadyComplete() {
        SudokuGame game = new SudokuGame();

        int[][] solvedGrid = createSolvedGrid();
        int[][] grid = copyGrid(solvedGrid);

        grid[5][5] = 1;

        Scanner scanner = new Scanner("hint\nquit\n");

        boolean result = game.gameLoop(scanner, grid, solvedGrid);

        assertFalse(result);
        assertTrue(output().contains("No hint available. Puzzle may already be complete."));
    }

    @Test
    void gameLoop_shouldReturnFalse_whenPuzzleIsCompleted() {
        SudokuGame game = new SudokuGame();

        int[][] solvedGrid = createSolvedGrid();
        int[][] grid = copyGrid(solvedGrid);

        // one missing value
        grid[0][1] = 0;

        // insert correct value, then extra newline for "Press any key to play again..."
        Scanner scanner = new Scanner("A2 3\nx\n");

        boolean result = game.gameLoop(scanner, grid, solvedGrid);

        assertTrue(result);
        assertEquals(3, grid[0][1]);
        assertTrue(output().contains("You have successfully completed the Sudoku puzzle!"));
        assertTrue(output().contains("Press any key to play again..."));
    }

    @Test
    void gameLoop_shouldHandleInvalidCommandGracefully() {
        SudokuGame game = new SudokuGame();

        int[][] grid = createPuzzleGrid();
        int[][] copiedOriginal = copyGrid(grid);

        Scanner scanner = new Scanner("wrong-command\nquit\n");

        boolean result = game.gameLoop(scanner, grid, copiedOriginal);

        assertFalse(result);
        assertFalse(output().isBlank());
    }

    private String output() {
        return outputStream.toString();
    }

    private int[][] copyGrid(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }

    private int[][] createPuzzleGrid() {
        int[][] grid = new int[9][9];
        grid[0][0] = 5; // A1 pre-filled
        grid[1][1] = 7; // B2 pre-filled
        return grid;
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
