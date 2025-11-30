package Verifier1.Checker;

import java.util.*;

public class BoxChecker extends Checker {

    public BoxChecker(int[] values, int num) {
        super(values, num);
    }

    @Override
    public void run() {
        findViolations(values);
    }

    public static int[] collectInts(int[][] board, int boxIndex) {
        int[] box = new int[9];
        int startRow = (boxIndex / 3) * 3; // the / floors the value so 0-3 is 0 , 4-6 is 1, 7-9 is 2 then *3
        int startCol = (boxIndex % 3) * 3; // % for boxes: 4, 5, 6, 7, 8, 9
        int pos = 0;

        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                box[pos++] = board[i][j];
            }
        }
        return box;
    }

    @Override
    protected void findViolations(int[] numbers) {
        for (int j = 1; j <= 9; j++) {
            Set<Integer> positions = new HashSet<>();
            for (int k = 0; k < numbers.length; k++) {
                if (numbers[k] == j) {
                    positions.add(k + 1);
                }
            }
            if (positions.size() > 1) {
                Violation violation = new Violation(num, j, positions);
                addViolation(violation);
            }
        }
    }

    @Override
    protected synchronized void addViolation(Violation violation) {
        boxViolations.add(violation);
    }
}