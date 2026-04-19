package org.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuBoardCreator {
    SudokuValidation sv = new SudokuValidation();
    private static final Random random = new Random();

    public boolean fillTheGrid(int[][] grid) {
        for (int row = 0; row < 9; row++){
            for (int col = 0; col < 9; col++){
                if (grid[row][col] == 0) {
                    List<Integer> numbers = randomDigitListCreator();
                    for (int num : numbers) {
                        if (sv.isValid(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (fillTheGrid(grid))
                                return true;
                            grid[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public int[][] copyTheGrid(int[][] gridOriginal){
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++)
            copy[i] = gridOriginal[i].clone();
        return copy;
    }
    private void backupRemovingCells(){

    }

    private List<Integer> randomDigitListCreator(){
        List<Integer> digits = new ArrayList<>(9);
        for (int i = 1; i <= 9; i++) {
            digits.add(i);
        }
        Collections.shuffle(digits);

        return digits;
    }

    public void printTheGrid(int[][] grid){
        char letter = 'A';
        for (int row=0; row<10; row++) {
            for (int col=0; col<10; col++) {
                if (row==0) {
                    if (col == 0)
                        System.out.print("  ");
                    else
                        System.out.print(col+" ");
                }else {
                    if (col==0) {
                        System.out.print(letter+" ");
                        letter++;
                    }else {
                        if (grid[row-1][col-1] == 0)
                            System.out.print("_ ");
                        else
                            System.out.print(grid[row-1][col-1]+" ");
                    }
                }
            }
            System.out.println();
        }
    }

    public void removeKDigits(int[][] grid, int k){
        while(k>0) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            if (grid[row][col] != 0){
                int backup = grid[row][col];
                grid[row][col] = 0;
                k--;
            }
        }
    }

    public List<int[]> findPreFilledCells(int[][] gridPuzzle){
        List<int[]> preFilledCells = new ArrayList<>();

        for (int row=0; row<9; row++){
            for (int col=0; col<9; col++){
                if (gridPuzzle[row][col]!=0){
                    preFilledCells.add(new int[]{row, col});
                }
            }
        }
        return preFilledCells;
    }

    private static boolean isPredefinedCell(int row, int col, List<int[]> predefined) {
        for (int[] cell : predefined) {
            if (cell[0] == row && cell[1] == col) {
                return true;
            }
        }
        return false;
    }

    public static boolean giveHint(int[][] grid, int[][] solution, List<int[]> predefined) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0 && !isPredefinedCell(row, col, predefined)) {
                    grid[row][col] = solution[row][col];
                    return true;
                }
            }
        }
        return false;
    }

}
