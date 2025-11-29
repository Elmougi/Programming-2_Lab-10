package VerifyModes;

import Checker.*;
import java.lang.Thread;

public class Mode3 extends Verifier {
    private Mode3(int[][] board) {
        super(board);
    }

    public static Verifier getInstance(int[][] board) {
        if (instance == null) {
            instance = new Mode3(board);
        }
        return instance;
    }

    @Override
    public Result verify() {
        Thread rows = new Thread() {
            public void run() {
                for (int i = 0; i < 9; i++) {
                    int[] row = RowChecker.collectInts(board, i);
                    RowChecker rowChecker = new RowChecker(row, i + 1);
                    rowChecker.run();
                }
            }
        };
        Thread columns = new Thread() {
            public void run() {
                for (int i = 0; i < 9; i++) {
                    int[] column = ColumnChecker.collectInts(board, i);
                    ColumnChecker columnChecker = new ColumnChecker(column, i + 1);
                    columnChecker.run();
                }
            }
        };
        Thread boxes = new Thread() {
            public void run() {
                for (int i = 0; i < 9; i++) {
                    int[] box = BoxChecker.collectsInts(board, i);
                    BoxChecker boxChecker = new BoxChecker(box, i + 1);
                    boxChecker.run();
                }
            }
        };

        rows.start();
        columns.start();
        boxes.start();

        try {
            rows.join();
            columns.join();
            boxes.join();
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // no race conditions because each thread will effect a separate list in the
        // Checker class

        return Checker.getResult();
    }
}
