package org.sudoku.game;

import org.sudoku.constant.Action;
import org.sudoku.exception.SudokuGenericException;
import org.sudoku.model.Command;
import org.sudoku.util.CommandParser;
import org.sudoku.util.SudokuGenerator;
import org.sudoku.util.SudokuValidation;

import java.text.MessageFormat;
import java.util.*;

public class SudokuGame {
    private static final int NO_OF_EMPTY_CELLS = 51;

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        int[][] grid, copiedOriginal;
        do {
            grid = new int[9][9];
            System.out.println("Welcome to Sudoku!\n");

            SudokuGenerator.fillTheGrid(grid);

            // Keep a copy of solved board for hints / validation
            copiedOriginal = SudokuGenerator.copyTheGrid(grid);

            // Remove numbers to create puzzle
            SudokuGenerator.removeKDigits(grid, NO_OF_EMPTY_CELLS);

            SudokuGenerator.printTheGrid(grid, "Here is your puzzle:");
        } while (this.gameLoop(scanner, grid, copiedOriginal));
        scanner.close();
    }

    public boolean gameLoop(Scanner scanner, int[][] grid, int[][] copiedOriginal) {
        // Find cells that must not be edited
        List<int[]> preFilledCells = SudokuGenerator.findPreFilledCells(grid);
        while (true) {
            System.out.println("\nEnter command (e.g., A3 4, C5 clear, hint, check, quit):");
            String commandStr = scanner.nextLine().trim();

            try {
                Command command = CommandParser.parse(commandStr);
                if ((Action.MOVE_NUM.equals(command.getAction()) || Action.CLEAR_NUM.equals(command.getAction()))
                        && SudokuValidation.isPredefinedCell(command, preFilledCells)) {
                    System.out.printf("Invalid %s. %s is pre-filled.\n", command.getAction().getCode(), command.getCellRef());
                    continue;
                }

                switch (command.getAction()) {
                    case MOVE_NUM:
                        grid[command.getRow()][command.getColumn()] = command.getValue();
                        System.out.println("Move accepted.");
                        SudokuGenerator.printTheGrid(grid, "Current grid:\n");
                        break;
                    case CLEAR_NUM:
                        grid[command.getRow()][command.getColumn()] = 0;
                        System.out.printf("%s cleared.\n", command.getCellRef());
                        SudokuGenerator.printTheGrid(grid, "Current grid:\n");
                        break;
                    case HINT:
                        Optional<String> hint = this.giveHint(grid, copiedOriginal);
                        if (hint.isPresent()) {
                            System.out.println(hint.get());
                        } else {
                            System.out.println("No hint available. Puzzle may already be complete.");
                        }
                        break;
                    case CHECK:
                        System.out.println(SudokuValidation.validateEntireGrid(grid));
                        break;
                    case QUIT:
                        System.out.println("Thanks for playing Sudoku!");
                        return false;
                }

                if (SudokuValidation.isPuzzleComplete(grid, copiedOriginal)) {
                    System.out.println("You have successfully completed the Sudoku puzzle!");
                    System.out.println("Press any key to play again...");
                    scanner.nextLine();
                    System.out.println("\n\n");
                    return true;
                }
            } catch (SudokuGenericException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private Optional<String> giveHint(int[][] gridPuzzle, int[][] copiedGrid) {
        List<int[]> empltyCells = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (gridPuzzle[row][col] == 0) {
                    empltyCells.add(new int[]{row, col});
                }
            }
        }
        if (empltyCells.isEmpty()) {
            return Optional.empty();
        } else {
            Random random = new Random();
            int hint = random.nextInt(empltyCells.size());
            int[] hintCell = empltyCells.get(hint);

            int hintValue = copiedGrid[hintCell[0]][hintCell[1]];
            return Optional.of(MessageFormat.format("Hint: Cell {0} = {1}",
                    String.valueOf((char) ('A' + hintCell[0])) + hintCell[1], hintValue));
        }
    }

}
