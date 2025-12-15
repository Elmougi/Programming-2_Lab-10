package Checker;

import java.util.*;

import VerifyModes.Result;

public abstract class Checker implements Runnable {
    protected int[] values;
    protected int num; // the value of row/column/box (1 to 9)
    // lists of all violations
    protected List<Violation> rowViolations = new ArrayList<>();
    protected List<Violation> colViolations = new ArrayList<>();
    protected List<Violation> boxViolations = new ArrayList<>();

    protected Checker(int[] values, int num) {
        if (values == null || values.length != 9) {
            throw new IllegalArgumentException("Values must be an array of length 9.");
        }
        if (num < 1 || num > 9) {
            throw new IllegalArgumentException("num must be between 1 and 9.");
        }

        this.values = values;
        this.num = num;
    }

    protected abstract void findViolations(int[] numbers);

    // protected abstract void addViolation(Violation violation); // will be
    // synchronised when overriden
    // must be static for the lock to be on any access
    // when it was not static, lock did not work because it was lock per
    // instance!!!!
    // hence, I had to remove the abstract declaration to allow static declaration
    // in each child (row, col, box)

    public void clearViolations() {
        rowViolations.clear();
        colViolations.clear();
        boxViolations.clear();
    } // just for safety

    public Result getResult() {
        return new Result(rowViolations, colViolations, boxViolations);
    }
}