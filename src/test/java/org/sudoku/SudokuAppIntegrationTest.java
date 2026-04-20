package org.sudoku;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SudokuAppIntegrationTest {

    @Test
    void quitWhenUserEntersQuit() {
        String input = "quit\n";

        String output = runAppWithInput(input);

        assertTrue(output.contains("Welcome to Sudoku!"));
        assertTrue(output.contains("Here is your puzzle:"));
        assertTrue(output.contains("Enter command"));
        assertTrue(output.contains("Thanks for playing Sudoku!"));
    }

    @Test
    void shouldShowHintWhenUserEntersHint() {
        String input = "hint\nquit\n";

        String output = runAppWithInput(input);

        assertTrue(output.contains("Welcome to Sudoku!"));
        assertTrue(output.contains("Here is your puzzle:"));
        assertTrue(output.contains("Hint: Cell "));
        assertTrue(output.contains("Thanks for playing Sudoku!"));
    }

    @Test
    void shouldCheckGridWhenUserEntersCheck() {
        String input = "check\nquit\n";

        String output = runAppWithInput(input);

        assertTrue(output.contains("Enter command"));
        assertTrue(
                output.contains("No rule violations detected.")
                        || output.contains("already exists in Row")
                        || output.contains("already exists in Column")
                        || output.contains("already exists in the 3x3 subgrid.")
        );
        assertTrue(output.contains("Thanks for playing Sudoku!"));
    }

    @Test
    void shouldRejectInvalidCommandFormat() {
        String input = "A1 5 extra\nquit\n";

        String output = runAppWithInput(input);

        assertTrue(output.contains("Invalid command format. Examples: B3 7, C5 clear, hint, check, quit"));
        assertTrue(output.contains("Thanks for playing Sudoku!"));
    }

    @Test
    void shouldRejectInvalidCellReference() {
        String input = "J1 5\nquit\n";

        String output = runAppWithInput(input);

        assertTrue(output.contains("Invalid cell reference. Cell reference should be within A1- I9."));
        assertTrue(output.contains("Thanks for playing Sudoku!"));
    }

    @Test
    void shouldHandleClearCommandOrRejectPrefilledCell() {
        String input = "A1 clear\nquit\n";

        String output = runAppWithInput(input);

        assertTrue(
                output.contains("A1 cleared.")
                        || output.contains("Invalid move. A1 is pre-filled.")
                        || output.contains("Invalid clear. A1 is pre-filled.")
        );
        assertTrue(output.contains("Thanks for playing Sudoku!"));
    }

    @Test
    void shouldAcceptNumericMoveOrRejectPrefilledCell() {
        String input = "A1 5\nquit\n";

        String output = runAppWithInput(input);

        assertTrue(
                output.contains("Move accepted.")
                        || output.contains("Invalid move. A1 is pre-filled.")
        );
        assertTrue(output.contains("Thanks for playing Sudoku!"));
    }

    private String runAppWithInput(String input) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();

        PrintStream originalOut = System.out;
        java.io.InputStream originalIn = System.in;

        System.setIn(testIn);
        System.setOut(new PrintStream(testOut));

        try {
            SudokuApp.main(new String[0]);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        return testOut.toString();
    }
}
