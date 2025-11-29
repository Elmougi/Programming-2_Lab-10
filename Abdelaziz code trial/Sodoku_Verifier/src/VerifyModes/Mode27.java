package VerifyModes;

import Checker.*;

public class Mode27 extends Verifier {
    private Mode27(int[][] board) {
        super(board);
    }

    public static Verifier getInstance(int[][] board) {
        if (instance == null) {
            instance = new Mode27(board);
        }
        return instance;
    }

    @Override
    public Result verify() {
        for (int i = 0; i < 9; i++) {
            int[] row = RowChecker.collectInts(board, i);
            RowChecker rowChecker = new RowChecker(row, i + 1, true);
            Thread t = new Thread(rowChecker);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupt exception catched");
            }
        }
        for (int i = 0; i < 9; i++) {
            int[] column = ColumnChecker.collectInts(board, i);
            ColumnChecker columnChecker = new ColumnChecker(column, i + 1, true);
            Thread t = new Thread(columnChecker);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupt exception catched");
            }
        }
        for (int i = 0; i < 9; i++) {
            int[] box = BoxChecker.collectsInts(board, i);
            BoxChecker boxChecker = new BoxChecker(box, i + 1, true);
            Thread t = new Thread(boxChecker);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupt exception catched");
            }
        }

        return Checker.getResult();
    }
}
