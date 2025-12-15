package Checker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Board.*;

public class SudokuIntegerChecker {
    final public SodokuBoard<Integer> board;
    protected List<Violation<Integer>> rowViolations = new ArrayList<>();
    protected List<Violation<Integer>> colViolations = new ArrayList<>();
    protected List<Violation<Integer>> boxViolations = new ArrayList<>();

    public SudokuIntegerChecker(SodokuBoard<Integer> board) {
        this.board = board;
        rowViolations.clear();
        colViolations.clear();
        boxViolations.clear();
    }

    public Result<Integer> getViolations() {
        RowIterator<Integer> rowIterator = (RowIterator<Integer>) board.rowIterator();
        ColumnIterator<Integer> colIterator = (ColumnIterator<Integer>) board.columnIterator();
        BoxIterator<Integer> boxIterator = (BoxIterator<Integer>) board.boxIterator();

        findViolations(rowIterator);
        findViolations(colIterator);
        findViolations(boxIterator);

        return new Result<>(rowViolations, colViolations, boxViolations);
    }

    private void findViolations(BoardIterator<Integer> iterator) {
        Set<Integer> positions = new HashSet<>();

        while (iterator.hasNextElement()) { // iterate on each row
            for (int i = 0; i < board.SIZE; i++) { // find each possible element value
                positions.clear();
                Integer value = board.board[0][0];

                while (iterator.hasNextElement()) { // compare with each element in the row
                    Integer compareValue = board.board[0][0];
                    if (value.equals(compareValue)) {
                        positions.add(iterator.getElementNum());
                    }
                    compareValue = iterator.nextElement();
                }
                if (positions.size() > 1) {
                    Violation<Integer> violation = new Violation<>(iterator.getElementNum(), value, positions);
                    if (iterator instanceof BoxIterator) {
                        addBoxViolation(violation);
                    } else if (iterator instanceof ColumnIterator) {
                        addColViolation(violation);
                    } else if (iterator instanceof RowIterator) {
                        addRowViolation(violation);
                    } else {
                        throw new IllegalStateException("Unknown iterator type.");
                    }
                }
            }

            iterator.nextList(); // move to next row
        }

    }

    private synchronized void addRowViolation(Violation<Integer> violation) {
        rowViolations.add(violation);
    }

    private synchronized void addColViolation(Violation<Integer> violation) {
        colViolations.add(violation);
    }

    private synchronized void addBoxViolation(Violation<Integer> violation) {
        boxViolations.add(violation);
    }

}
