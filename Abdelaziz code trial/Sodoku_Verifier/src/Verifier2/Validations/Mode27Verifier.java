package Verifier2.Validations;

import Verifier2.Board.*;
import java.util.ArrayList;
import java.util.List;

public class Mode27Verifier extends SgVerifier {

    public Mode27Verifier(SgBoard board) {
        super(board);
    }

    @Override
    public void verify() {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            final int index = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    validateCells(board.getRow(index), "ROW", index + 1);
                }
            });
            threads.add(t);
            t.start();
        }

        for (int i = 0; i < 9; i++) {
            final int index = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    validateCells(board.getColumn(index), "COL", index + 1);
                }
            });
            threads.add(t);
            t.start();
        }

        for (int i = 0; i < 9; i++) {
            final int index = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    validateCells(board.getBox(index), "BOX", index + 1);
                }
            });
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}