package Checker;
import java.util.*;

public class BoxChecker extends Checker {
    private static BoxChecker instance;
    private int i;

    private BoxChecker(int[][] board, int i) {
        super(board);
        this.i = i;
    }

    @Override
    public Checker getInstance(int[][] board, int i) {
        if (instance == null) {
            instance = new BoxChecker(board, i);
        }

        return instance;
    }

    private int[] collectsInts(int boxIndex) {
        int[] box = new int[9];
        int startRow = (boxIndex / 3) * 3;
        int startCol = (boxIndex % 3) * 3;
        int pos = 0;

        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                box[pos++] = board[i][j];
            }
        }
        return box;
    }
}