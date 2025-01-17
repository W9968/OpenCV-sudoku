package com.walee.sudoku.service;

public class Solution {
    public static int[][] Grid = {
            {0,0,0,3,0,0,7,0,0},
            {0,0,7,4,0,9,0,1,0},
            {6,4,0,0,2,7,5,0,0},
            {1,0,0,0,0,0,0,7,5},
            {7,0,0,0,0,0,0,0,0},
            {4,8,9,7,1,5,0,0,0},
            {8,0,4,6,0,3,0,0,0},
            {0,0,0,8,4,0,6,9,0},
            {0,6,1,0,0,0,4,0,0}
    };

    int[][] board;
    int EMPTY = 0;
    int SIZE = 9;

    public Solution(int[][] board) {
        this.board = board.clone();
    }


    private boolean isInRow(int row, int number) {

        for (int i = 0; i < SIZE; i++)
            if (board[row][i] == number)
                return true;

        return false;
    }

    private boolean isInCol(int col, int number) {

        for (int i = 0; i < SIZE; i++)
            if (board[i][col] == number)
                return true;

        return false;
    }

    private boolean isInBox(int row, int col, int number) {

        int r = row - row % 3;
        int c = col - col % 3;
        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (board[i][j] == number)
                    return true;

        return false;
    }

    private boolean isOk(int row, int col, int number) {
        return !isInRow(row, number)  &&  !isInCol(col, number)  &&  !isInBox(row, col, number);
    }

    public boolean solve() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    for (int number = 1; number <= SIZE; number++) {
                        if (isOk(row, col, number)) {
                            board[row][col] = number;
                            if (solve())
                                return true;
                            else
                                board[row][col] = EMPTY;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

//        public boolean solve(int compte) {
//
//                if (compte>=81) return true;
//                    int row = compte /9;
//                    int col = compte %9;
//                    for (int number = 1; number <= SIZE; number++) {
//                        if (isOk(row, col, number)) {
//                            board[row][col] = number;
//                            if (solve(compte++))
//                                return true;
//                             else
//                                board[row][col] = EMPTY;
//                        }
//                    }
//                    return false;
//    }

    public void display() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print("{"+board[i][j]+"} ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
