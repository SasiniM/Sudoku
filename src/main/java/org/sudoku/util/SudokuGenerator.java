package org.sudoku.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class SudokuGenerator {

    private static final Random random = new Random();

    public static boolean fillTheGrid(int[][] grid) {
        for (int row = 0; row < 9; row++){
            for (int col = 0; col < 9; col++){
                if (grid[row][col] == 0) {
                    List<Integer> numbers = randomDigitListCreator();
                    for (int num : numbers) {
                        if (SudokuValidation.isValid(grid, row, col, num)) {
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

    public static int[][] copyTheGrid(int[][] gridOriginal) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++)
            copy[i] = gridOriginal[i].clone();
        return copy;
    }

    public static void printTheGrid(int[][] grid, String title) {
        System.out.println(title);
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


    public static void removeKDigits(int[][] grid, int k){
        while(k>0) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            if (grid[row][col] != 0){
                //int backup = grid[row][col];
                grid[row][col] = 0;
                k--;
            }
        }
    }

    public static List<int[]> findPreFilledCells(int[][] gridPuzzle){
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

    private static List<Integer> randomDigitListCreator(){
        List<Integer> digits = new ArrayList<>(9);
        for (int i = 1; i <= 9; i++) {
            digits.add(i);
        }
        Collections.shuffle(digits);

        return digits;
    }
}
