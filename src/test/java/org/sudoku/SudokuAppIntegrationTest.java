package org.sudoku;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SudokuAppIntegrationTest {

    private InputStream originalIn;
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        originalIn = System.in;
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void main_shouldStartGameAndQuitSuccessfully() {
        provideInput("quit\n");

        SudokuApp.main(new String[0]);

        String output = getOutput();

        assertTrue(output.contains("Welcome to Sudoku!"));
        assertTrue(output.contains("Here is your puzzle:"));
        assertTrue(output.contains("Enter command"));
        assertTrue(output.contains("Thanks for playing Sudoku!"));
    }

    @Test
    void main_shouldHandleInvalidCommandAndThenQuit() {
        provideInput("invalid-command\nquit\n");

        SudokuApp.main(new String[0]);

        String output = getOutput();

        assertTrue(output.contains("Welcome to Sudoku!"));
        assertTrue(output.contains("Here is your puzzle:"));
        assertTrue(output.contains("Invalid command format. Examples: B3 7, C5 clear, hint, check, quit"));
        assertTrue(output.contains("Thanks for playing Sudoku!"));
    }

    @Test
    void main_shouldHandleCheckCommandAndThenQuit() {
        provideInput("check\nquit\n");

        SudokuApp.main(new String[0]);

        String output = getOutput();

        assertTrue(output.contains("Welcome to Sudoku!"));
        assertTrue(output.contains("Here is your puzzle:"));
        assertTrue(output.contains("No rule violations detected."));
        assertTrue(output.contains("Thanks for playing Sudoku!"));
    }

    @Test
    void main_shouldHandleHintCommandAndThenQuit() {
        provideInput("hint\nquit\n");

        SudokuApp.main(new String[0]);

        String output = getOutput();

        assertTrue(output.contains("Welcome to Sudoku!"));
        assertTrue(output.contains("Here is your puzzle:"));
        assertTrue(output.contains("Hint: Cell"));
        assertTrue(output.contains("Thanks for playing Sudoku!"));
    }

    private void provideInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }

    private String getOutput() {
        return outputStream.toString(StandardCharsets.UTF_8);
    }

}
