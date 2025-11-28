package Checker;

import java.util.*;

public class RowChecker extends Checker {
    private static RowChecker instance;
    private int i;

    private RowChecker(int[][] board, int i) {
        super(board);
        this.i = i;
    }

    @Override
    public Checker getInstance(int[][] board, int i) {
        if (instance == null) {
            instance = new RowChecker(board, i);
        }

        return instance;
    }

    @Override
    public void run() {
        int[] row = board[i];
        findViolations(row, i);
    }

    @Override
    protected void findViolations(int[] numbers, int index) {

    }
}