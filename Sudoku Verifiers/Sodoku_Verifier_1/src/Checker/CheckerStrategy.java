package Checker;

import java.util.ArrayList;
import java.util.List;

import Board.*;

public abstract class CheckerStrategy {
    final public Board<Integer> board;
    protected BoardIterator<Integer> iterator = null;
    protected List<Violation> violations = new ArrayList<>();

    public CheckerStrategy(Board<Integer> board) {
        this.board = board;
        violations.clear();
    }

    abstract protected void findViolations();

    public List<Violation> getViolations() {
        if (iterator == null) {
            throw new IllegalStateException("Iterator not set. Please set the iterator before getting violations.");
        }

        findViolations();

        if (iterator.hasNext()) {
            throw new IllegalStateException("Not all elements have been processed.");
        }

        return violations;
    }

}
