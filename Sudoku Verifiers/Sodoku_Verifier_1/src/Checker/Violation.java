package Checker;

import java.util.Set;

public class Violation<T> implements Comparable<Violation<T>> {
    private int index; // 1 to 9; we have 9 of each
    private T value; // number in violation
    private Set<Integer> positions; // set to have them arranged for easiness

    Violation(int index, T value, Set<Integer> positions) {
        this.index = index;
        this.value = value;
        setPositions(positions);
    }

    public void setPositions(Set<Integer> positions) {
        if (positions == null || positions.isEmpty()) {
            throw new IllegalArgumentException("Positions cannot be null or empty");
        }

        this.positions = positions;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value.toString();
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

    @Override
    public int compareTo(Violation<T> other) { // so I can use the built in Collections.sort()
        return Integer.compare(this.index, other.index);
    }
}
