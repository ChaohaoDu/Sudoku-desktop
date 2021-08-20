package com.chaohaodu.sudoku.computationlogic;


import com.chaohaodu.sudoku.constants.GameState;
import com.chaohaodu.sudoku.constants.Rows;
import com.chaohaodu.sudoku.problemdomain.SudokuGame;

import java.util.*;


public class GameLogic {

    public static SudokuGame getNewGame() {
        return new SudokuGame(GameState.NEW, GameGenerator.getNewGameGrid());
    }


    public static GameState  checkForCompletion(int[][] grid) {
        if (sudokuIsInvalid(grid) || tilesAreNotFilled(grid)) {
            return GameState.ACTIVE;
        }

        return GameState.COMPLETE;
    }

    public static boolean tilesAreNotFilled(int[][] grid) {
        for (int xIndex = 0; xIndex < SudokuGame.GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < SudokuGame.GRID_BOUNDARY; yIndex++) {
                if (grid[xIndex][yIndex] == 0) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean sudokuIsInvalid(int[][] grid) {
        return rowsAreInvalid(grid) || columnsAreInvalid(grid) || squaresAreInvalid(grid);
    }

    public static boolean squaresAreInvalid(int[][] grid) {
        return rowOfSquaresIsInvalid(Rows.TOP, grid) || rowOfSquaresIsInvalid(Rows.MIDDLE, grid)
           || rowOfSquaresIsInvalid(Rows.BOTTOM, grid);
    }

    private static boolean rowOfSquaresIsInvalid(Rows value, int[][] grid) {
        switch (value) {
            case TOP:
                return squareIsInvalid(0, 0, grid) || squareIsInvalid(0, 3, grid)
                   || squareIsInvalid(0, 6, grid);

            case MIDDLE:
                return squareIsInvalid(3, 0, grid) || squareIsInvalid(3, 3, grid)
                   || squareIsInvalid(3, 6, grid);

            case BOTTOM:
                return squareIsInvalid(6, 0, grid) || squareIsInvalid(6, 3, grid)
                   || squareIsInvalid(6, 6, grid);

            default:
                return false;
        }
    }

    public static boolean squareIsInvalid(int x, int y, int[][] grid) {
        int yIndexEnd = x + 3;
        int xIndexEnd = y + 3;

        List<Integer> square = new ArrayList<>();

        while (x < yIndexEnd) {

            while (y < xIndexEnd) {
                square.add(grid[y][x]);
                y++;
            }

            y -= 3;

            x++;
        }

        return collectionHasRepeats(square);
    }

    public static boolean columnsAreInvalid(int[][] grid) {
        for (int xIndex = 0; xIndex < SudokuGame.GRID_BOUNDARY; xIndex++) {
            List<Integer> row = new ArrayList<>();
            for (int yIndex = 0; yIndex < SudokuGame.GRID_BOUNDARY; yIndex++) {
                row.add(grid[xIndex][yIndex]);
            }

            if (collectionHasRepeats(row)) {
                return true;
            }
        }

        return false;
    }

    public static boolean rowsAreInvalid(int[][] grid) {
        for (int yIndex = 0; yIndex < SudokuGame.GRID_BOUNDARY; yIndex++) {
            List<Integer> row = new ArrayList<>();
            for (int xIndex = 0; xIndex < SudokuGame.GRID_BOUNDARY; xIndex++) {
                row.add(grid[xIndex][yIndex]);
            }

            if (collectionHasRepeats(row)) {
                return true;
            }
        }

        return false;
    }

    public static boolean collectionHasRepeats(List<Integer> collection) {
        for (int index = 1; index <= SudokuGame.GRID_BOUNDARY; index++) {
            if (Collections.frequency(collection, index) > 1) {
                return true;
            }
        }

        return false;
    }
}
