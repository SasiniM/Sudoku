package org.sudoku.util;

import org.junit.jupiter.api.Test;
import org.sudoku.constant.Action;
import org.sudoku.exception.SudokuGenericException;
import org.sudoku.model.Command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommandParserTest {
    @Test
    void parse_shouldReturnHintAction_whenHintCommandGiven() {
        Command command = CommandParser.parse("hint");

        assertEquals(Action.HINT, command.getAction());
        assertEquals("hint", command.getRawCommand());
    }

    @Test
    void parse_shouldReturnCheckAction_whenCheckCommandGiven() {
        Command command = CommandParser.parse("check");

        assertEquals(Action.CHECK, command.getAction());
        assertEquals("check", command.getRawCommand());
    }

    @Test
    void parse_shouldReturnQuitAction_whenQuitCommandGiven() {
        Command command = CommandParser.parse("quit");

        assertEquals(Action.QUIT, command.getAction());
        assertEquals("quit", command.getRawCommand());
    }

    @Test
    void parse_shouldParseInsertCommand() {
        Command command = CommandParser.parse("A2 4");

        assertEquals(Action.MOVE_NUM, command.getAction());
        assertEquals("A2", command.getCellRef());
        assertEquals(0, command.getRow());
        assertEquals(1, command.getColumn());
        assertEquals(4, command.getValue());
        assertEquals("A2 4", command.getRawCommand());
    }

    @Test
    void parse_shouldParseInsertCommandWithLowerCaseCellReference() {
        Command command = CommandParser.parse("b3 7");

        assertEquals(Action.MOVE_NUM, command.getAction());
        assertEquals("B3", command.getCellRef());
        assertEquals(1, command.getRow());
        assertEquals(2, command.getColumn());
        assertEquals(7, command.getValue());
        assertEquals("b3 7", command.getRawCommand());
    }

    @Test
    void parse_shouldParseClearCommand() {
        Command command = CommandParser.parse("C5 clear");

        assertEquals(Action.CLEAR_NUM, command.getAction());
        assertEquals("C5", command.getCellRef());
        assertEquals(2, command.getRow());
        assertEquals(4, command.getColumn());
        assertEquals(0, command.getValue());
        assertEquals("C5 clear", command.getRawCommand());
    }

    @Test
    void parse_shouldParseClearCommandIgnoringCase() {
        Command command = CommandParser.parse("d7 CLEAR");

        assertEquals(Action.CLEAR_NUM, command.getAction());
        assertEquals("D7", command.getCellRef());
        assertEquals(3, command.getRow());
        assertEquals(6, command.getColumn());
        assertEquals(0, command.getValue());
    }

    @Test
    void parse_shouldSupportMultipleSpacesBetweenTokens() {
        Command command = CommandParser.parse("A2    9");

        assertEquals(Action.MOVE_NUM, command.getAction());
        assertEquals("A2", command.getCellRef());
        assertEquals(9, command.getValue());
    }

    @Test
    void parse_shouldThrowException_whenCellReferenceIsInvalid() {
        SudokuGenericException exception = assertThrows(
                SudokuGenericException.class,
                () -> CommandParser.parse("J2 4")
        );

        assertEquals("Invalid cell reference. Cell reference should be within A1- I9.", exception.getMessage());
    }

    @Test
    void parse_shouldThrowException_whenNumberIsZero() {
        SudokuGenericException exception = assertThrows(
                SudokuGenericException.class,
                () -> CommandParser.parse("A2 0")
        );

        assertEquals("Invalid number. Enter a value from 1 to 9.", exception.getMessage());
    }

    @Test
    void parse_shouldThrowException_whenNumberIsGreaterThanNine() {
        SudokuGenericException exception = assertThrows(
                SudokuGenericException.class,
                () -> CommandParser.parse("A2 10")
        );

        assertEquals("Invalid number. Enter a value from 1 to 9.", exception.getMessage());
    }

    @Test
    void parse_shouldThrowException_whenNumberIsNotNumeric() {
        SudokuGenericException exception = assertThrows(
                SudokuGenericException.class,
                () -> CommandParser.parse("A2 x")
        );

        assertEquals("Invalid number. Enter a value from 1 to 9.", exception.getMessage());
    }

    @Test
    void parse_shouldThrowException_whenSingleWordCommandIsUnknown() {
        SudokuGenericException exception = assertThrows(
                SudokuGenericException.class,
                () -> CommandParser.parse("play")
        );

        assertEquals("Invalid command format. Examples: B3 7, C5 clear, hint, check, quit", exception.getMessage());
    }

    @Test
    void parse_shouldThrowException_whenCommandHasTooManyTokens() {
        SudokuGenericException exception = assertThrows(
                SudokuGenericException.class,
                () -> CommandParser.parse("A2 4 extra")
        );

        assertEquals("Invalid command format. Examples: B3 7, C5 clear, hint, check, quit", exception.getMessage());
    }

    @Test
    void parse_shouldThrowException_whenSecondTokenIsUnknownAction() {
        SudokuGenericException exception = assertThrows(
                SudokuGenericException.class,
                () -> CommandParser.parse("A2 remove")
        );

        assertEquals("Invalid number. Enter a value from 1 to 9.", exception.getMessage());
    }
}
