package Controller_Layer.Checker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Controller_Layer.Board.*;

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
        //System.out.println("Finished rows");
        findViolations(colIterator);
        //System.out.println("Finished columns");
        findViolations(boxIterator);
        //System.out.println("Finished boxes");

        return new Result<>(rowViolations, colViolations, boxViolations);
    }

    private void findViolations(BoardIterator<Integer> iterator) {
        Set<Integer> positions = new HashSet<>();
        int listIndex = 0;

        do { // iterate on each list
            for (Integer value = 1; value <= board.SIZE; value++) { // find each possible element value
                positions.clear();
                iterator.resetList();
                //System.out.println(board.SIZE);

                // System.out.println("Checking for value: " + value + " in " +
                // iterator.getClass().getSimpleName() + " #" + iterator.getElementNum());
                while (iterator.hasNextElement()) {
                    // System.out.println("entered");
                    Integer compareValue = iterator.nextElement();
                    // System.out.println(
                    //         "element: " + iterator.getElementNum() + "; Comparing " + compareValue + " with " + value);
                    if (value.equals(compareValue)) {
                        positions.add(iterator.getElementNum());
                        // System.out.println("Found value " + value + " at position " + iterator.getElementNum() +
                        //         " in " + iterator.getClass().getSimpleName() + " #" + (listIndex+1));

                        //System.out.println(positions.toString());
                    }
                    // System.out.println("passed");
                }

                // System.out.println("Found positions for value " + value + ": " + positions);
                // System.out.println("out!");
                if (positions.size() > 1) {
                    //System.out.println(positions.toString());
                    Violation<Integer> violation = new Violation<>(listIndex + 1, value, positions);
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

            if (iterator.hasNextList()) {
                listIndex = iterator.nextList();
            } else {
                break;
            }
        } while (true);
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
