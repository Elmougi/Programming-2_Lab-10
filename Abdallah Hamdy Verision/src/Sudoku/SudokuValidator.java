package Sudoku;

import Checkers.*;
import CheckerFactory.*;
import java.util.ArrayList;
import java.util.List;



public class SudokuValidator {

    private final SudokuBoard board;
    private final List<Checker> allCheckers = new ArrayList<>();

    public SudokuValidator(SudokuBoard board) {
        this.board = board;
    }

    // Main validation entry
    public void validate(int mode) throws InterruptedException {
        if (mode == 0) {
            runSequential();
        } else if (mode == 3) {
            run3Threads();
        } else if (mode == 27) {
            run27Threads();
        } else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }

        printResult();
    }

    
    private void runSequential() {
        allCheckers.clear();

        for (int i = 0; i < 9; i++) {
            Checker c = CheckerFactory.createChecker(
                    CheckerFactory.ROW,
                    i + 1,
                    board.getRow(i)
            );
            c.run();
            allCheckers.add(c);
        }

        for (int i = 0; i < 9; i++) {
            Checker c = CheckerFactory.createChecker(
                    CheckerFactory.COLUMN,
                    i + 1,
                    board.getColumn(i)
            );
            c.run();
            allCheckers.add(c);
        }

        for (int i = 0; i < 9; i++) {
            Checker c = CheckerFactory.createChecker(
                    CheckerFactory.BOX,
                    i + 1,
                    board.getBox(i)
            );
            c.run();
            allCheckers.add(c);
        }
    }

    private void run3Threads() throws InterruptedException {
        allCheckers.clear();

        Thread rowThread = new Thread(() -> {
            for (int i = 0; i < 9; i++) {
                Checker c = CheckerFactory.createChecker(
                        CheckerFactory.ROW,
                        i + 1,
                        board.getRow(i)
                );
                c.run();
                synchronized (allCheckers) { allCheckers.add(c); }
            }
        });

        Thread colThread = new Thread(() -> {
            for (int i = 0; i < 9; i++) {
                Checker c = CheckerFactory.createChecker(
                        CheckerFactory.COLUMN,
                        i + 1,
                        board.getColumn(i)
                );
                c.run();
                synchronized (allCheckers) { allCheckers.add(c); }
            }
        });

        Thread boxThread = new Thread(() -> {
            for (int i = 0; i < 9; i++) {
                Checker c = CheckerFactory.createChecker(
                        CheckerFactory.BOX,
                        i + 1,
                        board.getBox(i)
                );
                c.run();
                synchronized (allCheckers) { allCheckers.add(c); }
            }
        });

        rowThread.start();
        colThread.start();
        boxThread.start();

        rowThread.join();
        colThread.join();
        boxThread.join();
    }

    private void run27Threads() throws InterruptedException {
        allCheckers.clear();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            final int index = i;
            Thread t = new Thread(() -> {
                Checker c = CheckerFactory.createChecker(
                        CheckerFactory.ROW,
                        index + 1,
                        board.getRow(index)
                );
                c.run();
                synchronized (allCheckers) { allCheckers.add(c); }
            });
            threads.add(t);
        }

        for (int i = 0; i < 9; i++) {
            final int index = i;
            Thread t = new Thread(() -> {
                Checker c = CheckerFactory.createChecker(
                        CheckerFactory.COLUMN,
                        index + 1,
                        board.getColumn(index)
                );
                c.run();
                synchronized (allCheckers) { allCheckers.add(c); }
            });
            threads.add(t);
        }

        for (int i = 0; i < 9; i++) {
            final int index = i;
            Thread t = new Thread(() -> {
                Checker c = CheckerFactory.createChecker(
                        CheckerFactory.BOX,
                        index + 1,
                        board.getBox(index)
                );
                c.run();
                synchronized (allCheckers) { allCheckers.add(c); }
            });
            threads.add(t);
        }

        for (Thread t : threads) t.start();

        for (Thread t : threads) t.join();
    }

    private void printResult() {
        boolean valid = allCheckers.stream().allMatch(c -> c.getDuplicates().isEmpty());

        if (valid) {
            System.out.println("VALID");
            return;
        }

        System.out.println("INVALID");

        for (Checker c : allCheckers) {
            if (c instanceof RowChecker && !c.getDuplicates().isEmpty()) {
                System.out.println("ROW " + c.getId() + ", duplicates: " + c.getDuplicates());
            }
        }

        System.out.println("------------------------------------------");

        for (Checker c : allCheckers) {
            if (c instanceof ColumnChecker && !c.getDuplicates().isEmpty()) {
                System.out.println("COL " + c.getId() + ", duplicates: " + c.getDuplicates());
            }
        }

        System.out.println("------------------------------------------");

        for (Checker c : allCheckers) {
            if (c instanceof BoxChecker && !c.getDuplicates().isEmpty()) {
                System.out.println("BOX " + c.getId() + ", duplicates: " + c.getDuplicates());
            }
        }
    }
}
