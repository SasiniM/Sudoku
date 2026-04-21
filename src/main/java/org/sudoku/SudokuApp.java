package org.sudoku;

import org.sudoku.game.SudokuGame;

public class SudokuApp {

    public static void main(String[] args) {
        SudokuGame sudokuGame = new SudokuGame();
        sudokuGame.startGame();
    }
}