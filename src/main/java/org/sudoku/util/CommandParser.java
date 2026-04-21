package org.sudoku.util;

import org.sudoku.SudokuValidation;
import org.sudoku.constant.Action;
import org.sudoku.exception.InvalidCellReferenceException;
import org.sudoku.exception.InvalidCommandException;
import org.sudoku.exception.InvalidValueException;
import org.sudoku.model.Command;

public class CommandParser {
    // private static final String COMMAND_DELIMITER = " ";
    private static final String COMMAND_DELIMITER = "\\s+";

    public static Command parse(String command) {
        String[] splitCommand = command.split(COMMAND_DELIMITER);

        Command.CommandBuilder commandBuilder = Command.builder();

        commandBuilder.rawCommand(command);

        if (splitCommand.length == 1) {
            return commandBuilder.action(Action.of(splitCommand[0])).build();
        } else if (splitCommand.length == 2) {
            String cellRef = splitCommand[0].toUpperCase();
            if (!SudokuValidation.isValidCellReference(cellRef)) {
                throw new InvalidCellReferenceException("Invalid cell reference. Cell reference should be within A1- I9.");
            }
            commandBuilder.cellRef(cellRef);

            if (Action.CLEAR_NUM.getCode().equalsIgnoreCase(splitCommand[1])) {
                commandBuilder.action(Action.of(splitCommand[1])).build();
            } else {
                commandBuilder.action(Action.INSERT_NUM);
                if (!splitCommand[1].matches("[1-9]")) {
                    throw new InvalidValueException("Invalid number. Enter a value from 1 to 9.");
                }
                commandBuilder.value(Integer.parseInt(splitCommand[1]));
            }

            commandBuilder.row(cellRef.charAt(0) - 'A');
            commandBuilder.column(Character.getNumericValue(cellRef.charAt(1)) - 1);


            return commandBuilder.build();
        } else {
            throw new InvalidCommandException("Invalid command format. Expected: cellRef action");
        }
    }
}
