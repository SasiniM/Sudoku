package org.sudoku;

import org.sudoku.model.Command;

import java.util.*;

public class SudokuSolver {

    SudokuBoardCreator sbc = new SudokuBoardCreator();

    private static final char[] columns = new char[]{'A', 'B', 'C', 'D', 'E', 'F','G','H','I'};

    public String handleClear(int[][] grid, List<int[]> preFilledCells, int row, int col){
        grid[row][col] = 0;
        String cellRef = String.valueOf((columns[row]) + "" + (col + 1));
        if (preFilledCells.contains(new int[]{row, col})) {
            return "Invalid clear. " + cellRef + " is pre-filled.";
        }
        return cellRef + " cleared.";
    }

    public void handleClear(int[][] grid, Command command) {
        grid[command.getRow()][command.getColumn()] = 0;
        String cellRef = String.valueOf((columns[command.getRow()]) + "" + (command.getColumn() + 1));
    }

    /*public String giveHint(int[][] gridPuzzle, int[][] copiedGrid){
        List<int[]> empltyCells = new ArrayList<>();
        for (int row=0; row<9; row++){
            for (int col=0; col<9; col++){
                if (gridPuzzle[row][col]==0){
                    empltyCells.add(new int[]{row, col});
                }
            }
        }
        if (empltyCells.isEmpty()){
            return "No hint available. Puzzle may already be complete.";
        } else {
            Random random = new Random();
            int hint = random.nextInt(empltyCells.size());
            int[] hintCell = empltyCells.get(hint);

            int hintValue = copiedGrid[hintCell[0]][hintCell[1]];
            //gridPuzzle[hintCell[0]][hintCell[1]] = hintValue;
            return  "Hint: Cell " + ((char) columns[hintCell[0]]) + (hintCell[1] + 1) + " = " + hintValue;

        }
    }*/

    public Optional<Integer> giveHint(int[][] gridPuzzle, int[][] copiedGrid){
        List<int[]> empltyCells = new ArrayList<>();
        for (int row=0; row<9; row++){
            for (int col=0; col<9; col++){
                if (gridPuzzle[row][col]==0){
                    empltyCells.add(new int[]{row, col});
                }
            }
        }
        if (empltyCells.isEmpty()){
            return Optional.empty();
        } else {
            Random random = new Random();
            int hint = random.nextInt(empltyCells.size());
            int[] hintCell = empltyCells.get(hint);

            int hintValue = copiedGrid[hintCell[0]][hintCell[1]];
            //gridPuzzle[hintCell[0]][hintCell[1]] = hintValue;
            return Optional.of(hintValue);
            // return  "Hint: Cell " + ((char) columns[hintCell[0]]) + (hintCell[1] + 1) + " = " + hintValue;

        }
    }


}
