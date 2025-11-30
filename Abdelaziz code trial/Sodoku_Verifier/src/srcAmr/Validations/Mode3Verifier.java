package srcAmr.Validations;

import srcAmr.Board.*;

public class Mode3Verifier extends SgVerifier {

    public Mode3Verifier(SgBoard board) {
        super(board);
    }

    @Override
    public void verify() {

        Thread rowThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 9; i++) {
                    validateCells(board.getRow(i), "ROW", i + 1);
                }
            }
        });

        Thread colThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 9; i++) {
                    validateCells(board.getColumn(i), "COL", i + 1);
                }
            }
        });

        Thread boxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 9; i++) {
                    validateCells(board.getBox(i), "BOX", i + 1);
                }
            }
        });

        rowThread.start();
        colThread.start();
        boxThread.start();

        try {
            rowThread.join();
            colThread.join();
            boxThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}