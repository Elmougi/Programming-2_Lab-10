package Checker;

import java.util.*;

import VerifyModes.Result;

public abstract class Checker implements Runnable {
    protected int[] values;
    protected int num; // the value of row/column/box (1 to 9)
    // lists of all violations
    protected static List<Violation> rowViolations = new ArrayList<>();
    protected static List<Violation> colViolations = new ArrayList<>();
    protected static List<Violation> boxViolations = new ArrayList<>();

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

    protected abstract void addViolation(Violation violation); // will be synchronised when overriden

    public static Result getResult() {
        return new Result(rowViolations, colViolations, boxViolations);
    }
}