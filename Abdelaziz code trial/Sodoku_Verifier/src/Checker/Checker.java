package Checker;
import java.util.*;

import VerifyModes.Result;

public abstract class Checker implements Runnable {
    protected static Checker instance;
    protected int[] values;
    protected int num; // the value of row/column/box (1 to 9)
    // lists of all violations
    protected List<Violation> rowViolations = new ArrayList<>();
    protected List<Violation> colViolations = new ArrayList<>();
    protected List<Violation> boxViolations = new ArrayList<>();

    protected Checker(int[] values, int num) {
        if(values == null || values.length != 9) {
            throw new IllegalArgumentException("Values must be an array of length 9.");
        }
        if(num < 1 || num > 9) {
            throw new IllegalArgumentException("num must be between 1 and 9.");
        }
        
        this.values = values;
        this.num = num;
    }

    protected abstract void findViolations(int[] numbers);
    
    public Result getResult() {
        return new Result(rowViolations, colViolations, boxViolations);
    }
}