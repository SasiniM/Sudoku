package org.sudoku;

import org.sudoku.constant.Action;
import org.sudoku.model.Command;
import org.sudoku.util.CommandParser;
import org.sudoku.util.SudokuGenerator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class SudokuApp {
    private static final int NO_OF_EMPTY_CELLS = 20;

    SudokuSolver ss = new SudokuSolver();

    public static void main(String[] args) {
        SudokuBoardCreator sbc = new SudokuBoardCreator();
        SudokuSolver ss = new SudokuSolver();
        Scanner scanner = new Scanner(System.in);

        while (true) {

            int[][] grid = new int[9][9];
            System.out.println("Welcome to Sudoku!\n");

            // Create solved board first
            sbc.fillTheGrid(grid);

            // Keep a copy of solved board for hints / validation
            int[][] copiedOriginal = sbc.copyTheGrid(grid);

            // Remove numbers to create puzzle
            sbc.removeKDigits(grid, NO_OF_EMPTY_CELLS);

            // Find cells that must not be edited
            List<int[]> preFilledCells = sbc.findPreFilledCells(grid);

            System.out.println("Here is your puzzle:");
            sbc.printTheGrid(grid);

            boolean puzzleSolved = false;

            while(!puzzleSolved) {
                System.out.println();
                System.out.println("Enter command (e.g., A3 4, C5 clear, hint, check, quit):");
                String command = scanner.nextLine().trim();

                if (command.equalsIgnoreCase("quit")) {
                    System.out.println("Thanks for playing Sudoku!");
                    scanner.close();
                    return;
                }
                if (command.equalsIgnoreCase("hint")) {
                    System.out.println(ss.giveHint(grid, copiedOriginal));
                    continue;
                }

                if (command.equalsIgnoreCase("check")) {
                    String validationMessage = SudokuValidation.validateEntireGrid(grid);
                    if (validationMessage == null) {
                        System.out.println("No rule violations detected.");
                    } else {
                        System.out.println(validationMessage);
                    }
                    continue;
                }

                String[] parts = command.split("\\s+");
                if (parts.length != 2) {
                    System.out.println("Invalid command format. Examples: B3 7, C5 clear, hint, check, quit");
                    continue;
                }

                String cellRef = parts[0].toUpperCase();
                String action = parts[1].toLowerCase();

                if (!SudokuValidation.isValidCellReference(cellRef)) {
                    System.out.println("Invalid cell reference. Cell reference should be within A1- I9.");
                    continue;
                }

                int row = cellRef.charAt(0) - 'A';
                int col = Character.getNumericValue(cellRef.charAt(1)) - 1;

                if (SudokuValidation.isPredefinedCell(row, col, preFilledCells)) {
                    System.out.println("Invalid move. " + cellRef + " is pre-filled.");
                    continue;
                }

                if ("clear".equals(action)) {
                    System.out.println(ss.handleClear(grid, preFilledCells, row, col));
                    sbc.printTheGrid(grid);
                    continue;
                }

                if (!action.matches("[1-9]")) {
                    System.out.println("Invalid number. Enter a value from 1 to 9.");
                    continue;
                }

                int value = Integer.parseInt(action);
                grid[row][col] = value;

                System.out.println("Move accepted.");
                System.out.println("Current grid:\n");
                sbc.printTheGrid(grid);

                if (SudokuValidation.isPuzzleComplete(grid, copiedOriginal)) {
                    System.out.println("You have successfully completed the Sudoku puzzle!");
                    System.out.println("Press any key to play again...");
                    scanner.nextLine();
                    System.out.println("\n\n");
                    puzzleSolved = true;
                }
            }
        }
    }

    private void startGame(Scanner scanner) {
        int[][] grid = new int[9][9];
        System.out.println("Welcome to Sudoku!\n");

        SudokuGenerator.fillTheGrid(grid);

        // Keep a copy of solved board for hints / validation
        int[][] copiedOriginal = SudokuGenerator.copyTheGrid(grid);

        // Remove numbers to create puzzle
        SudokuGenerator.removeKDigits(grid, NO_OF_EMPTY_CELLS);

        // Find cells that must not be edited
        List<int[]> preFilledCells = SudokuGenerator.findPreFilledCells(grid);

        System.out.println("Here is your puzzle:");
        SudokuGenerator.printTheGrid(grid);

        //boolean puzzleSolved = false;
    }

    private boolean gameLoop(Scanner scanner, int[][] grid, int[][] copiedOriginal, List<int[]> preFilledCells) {
        boolean puzzleSolved = false;
        while(!puzzleSolved) {
            System.out.println();
            System.out.println("Enter command (e.g., A3 4, C5 clear, hint, check, quit):");
            String commandStr = scanner.nextLine().trim();

            Command command = CommandParser.parse(commandStr);

            if ((Action.INSERT_NUM.equals(command.getAction()) || Action.CLEAR_NUM.equals(command.getAction()))
                    && SudokuValidation.isPredefinedCell(command, preFilledCells)) {
                System.out.println("Invalid " + (Action.INSERT_NUM.equals(command.getAction()) ? "move" : "clear")
                        + ". " + command.getCellRef() + " is pre-filled.");
                continue;
            }

            switch (command.getAction()) {
                case INSERT_NUM:
                    grid[command.getRow()][command.getColumn()] = command.getValue();
                    System.out.println("Move accepted.");
                    System.out.println("Current grid:\n");
                    continue;
                case CLEAR_NUM:
                    ss.handleClear(grid, command);
                    System.out.println(command.getCellRef() + " cleared.");
                    continue;
                case HINT:
                    Optional<Integer> hintValue = ss.giveHint(grid, copiedOriginal);
                    if (hintValue.isPresent()) {
                        System.out.println("Hint: Cell " + command.getCellRef() + " = " + hintValue.get());
                    } else {
                        System.out.println("No hint available. Puzzle may already be complete.");
                    }
                    continue;
                case CHECK:
                    checkGrid(grid);
                    continue;
                case QUIT:
                    System.out.println("Thanks for playing Sudoku!");
                    scanner.close();
                    return true;
                default:
                    System.out.println("Invalid command");
                    break;
            };
        }
        return false;
    }

    private void checkGrid(int[][] grid) {
        String validationMessage = SudokuValidation.validateEntireGrid(grid);
        if (validationMessage == null) {
            System.out.println("No rule violations detected.");
        } else {
            System.out.println(validationMessage);
        }
    }
}