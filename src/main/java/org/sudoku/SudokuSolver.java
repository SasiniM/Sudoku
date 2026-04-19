package org.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class SudokuSolver {
    private static final Pattern cellClear = Pattern.compile(
            "^([A-I])([1-9])\\s+(clear)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern cellNumber = Pattern.compile(
            "^([A-I])([1-9])\\s+([1-9])$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern singleCommand =
            Pattern.compile("^(hint|check|quit)$", Pattern.CASE_INSENSITIVE);


    SudokuBoardCreator sbc = new SudokuBoardCreator();

    private static final char[] columns = new char[]{'A', 'B', 'C', 'D', 'E', 'F','G','H','I'};

    public boolean checkAnswer(int[][] gridBackup, int[][] gridPuzzle, int row, int col, int answer){
        if (answer == gridBackup[row][col]){
            gridPuzzle[row][col] = answer;
            return true;
        }else
            return false;
    }

    void handleCellNumber(){

    }
    private boolean handleClear(int[][] gridPuzzle, char row, int col, List<int[]> predefined){
        int[] clearingCell = {(row%65)-1, col};
        boolean found = predefined.stream()
                .anyMatch(arr -> Arrays.equals(arr, clearingCell));
        if (found) {
            System.out.println("Cannot clear a predefined value");
            return true;
        }else {
            gridPuzzle[(row%65)-1][col]=0;
            return false;
        }
    }
    String giveHint(int[][] gridPuzzle, int[][] copiedGrid){
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
    }


    void handleCheck(){

    }



}
