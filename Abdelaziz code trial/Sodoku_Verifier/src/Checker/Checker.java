package Checker;
import java.util.*;

public abstract class Checker implements Runnable {
    protected Checker instance;
    protected int[][] board;
    // lists of all violations
    protected List<Violation> rowViolations = new ArrayList<>();
    protected List<Violation> colViolations = new ArrayList<>();
    protected List<Violation> boxViolations = new ArrayList<>();

    protected Checker(int[][] board) {
        this.board = board;
    }

    protected abstract Checker getInstance(int[][] board, int i);

    protected abstract void findViolations(int[] numbers, int index);
    
}