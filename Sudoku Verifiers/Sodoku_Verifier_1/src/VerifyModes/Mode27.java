package Verifier1.VerifyModes;

import Verifier1.Checker.*;
import java.util.*;

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
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            int[] row = RowChecker.collectInts(board, i);
            RowChecker rowChecker = new RowChecker(row, i + 1);
            Thread t = new Thread(rowChecker);
            threads.add(t);
            t.start();
        }

        for (int i = 0; i < 9; i++) {
            int[] column = ColumnChecker.collectInts(board, i);
            ColumnChecker columnChecker = new ColumnChecker(column, i + 1);
            Thread t = new Thread(columnChecker);
            threads.add(t);
            t.start();
        }

        for (int i = 0; i < 9; i++) {
            int[] box = BoxChecker.collectInts(board, i);
            BoxChecker boxChecker = new BoxChecker(box, i + 1);
            Thread t = new Thread(boxChecker);
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupt exception catched");
            }
        }

        return Checker.getResult();
    }
}
