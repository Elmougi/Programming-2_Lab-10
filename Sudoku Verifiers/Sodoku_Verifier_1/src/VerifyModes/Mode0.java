package Verifier1.VerifyModes;

import Verifier1.Checker.*;

public class Mode0 extends Verifier {
    private Mode0(int[][] board) {
        super(board);
    }

    public static Verifier getInstance(int[][] board) {
        if (instance == null) {
            instance = new Mode0(board);
        }
        return instance;
    }

    @Override
    public Result verify() {
        for (int i = 0; i < 9; i++) {
            int[] row = RowChecker.collectInts(board, i);
            RowChecker rowChecker = new RowChecker(row, i + 1);
            rowChecker.run();

            int[] column = ColumnChecker.collectInts(board, i);
            ColumnChecker columnChecker = new ColumnChecker(column, i + 1);
            columnChecker.run();

            int[] box = BoxChecker.collectInts(board, i);
            BoxChecker boxChecker = new BoxChecker(box, i + 1);
            boxChecker.run();
        }

        return Checker.getResult();
    }
}
