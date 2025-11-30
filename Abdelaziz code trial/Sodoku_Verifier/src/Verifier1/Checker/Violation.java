package Verifier1.Checker;

import java.util.Set;

public class Violation {
    private int index; // 1 to 9; we have 9 of each
    private int number; // number in violation
    private Set<Integer> positions; // set to have them arranged for easiness

    Violation(int index, int number, Set<Integer> positions) {
        setIndex(index);
        setNumber(number);
        setPositions(positions);
    }

    public void setIndex(int index) {
        if (index < 1 || index > 9) {
            throw new IndexOutOfBoundsException("Index must be between 1 and 9");
        }

        this.index = index;
    }

    public void setNumber(int number) {
        if (number < 1 || number > 9) {
            throw new IllegalArgumentException("Number must be between 1 and 9");
        }

        this.number = number;
    }

    public void setPositions(Set<Integer> positions) {
        if (positions == null || positions.isEmpty()) {
            throw new IllegalArgumentException("Positions cannot be null or empty");
        }
        for (int pos : positions) {
            if (pos < 1 || pos > 9) {
                throw new IndexOutOfBoundsException("Position must be between 1 and 9");
            }
        }

        this.positions = positions;
    }

    public int getIndex() {
        return index;
    }

    public int getNumber() {
        return number;
    }

    public String getPositionsString() {
        StringBuilder s = new StringBuilder();
        Integer[] values = positions.toArray(new Integer[0]);
        for (int i = 0; i < values.length; i++) {
            s.append(values[i]);
            if (i < values.length - 1) { // there are values left
                s.append(", ");
            }
        }
        return s.toString();
    }
}
