package Checker;
import java.util.*;

public class ColumnChecker extends Checker {
    private static ColumnChecker instance;
    private int i;

    private ColumnChecker(int[][] board, int i) {
        super(board);
        this.i = i;
    }

    @Override
    public Checker getInstance(int[][] board, int i) {
        if (instance == null) {
            instance = new ColumnChecker(board, i);
        }

        return instance;
    }

    @Override
    public void run() {
        int[] column = new int[9];
        for (int i = 0; i < 9; i++) {
            column[i] = board[i][colIndex];
        }
        findViolations(column, colIndex + 1);
    }
}