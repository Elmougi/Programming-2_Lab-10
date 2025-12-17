package Controller_Layer.Checker;

import java.util.Set;

public class Violation<T> implements Comparable<Violation<T>> {
    private int index; // 1 to 9; we have 9 of each assuming size of array is 9
    private T value; // number in violation
    private String positions; // set to have them arranged for easiness

    Violation(int index, T value, Set<Integer> positions) {
        this.index = index + 1;
        this.value = value;
        this.positions = getPositionsString(setPositions(positions));
    }

    public Set<Integer> setPositions(Set<Integer> positions) {
        if (positions == null || positions.isEmpty()) {
            throw new IllegalArgumentException("Positions cannot be null or empty");
        }

        return positions;
    }

    public int getIndex() {
        return index;
    }

    public T getValue() {
        return value;
    }

    private String getPositionsString(Set<Integer> positions) {
        StringBuilder s = new StringBuilder();
        Integer[] values = positions.toArray(new Integer[positions.size()]);
        // System.out.println(values.length);
        for (int i = 0; i < values.length; i++) {
            s.append(values[i]);
            if (i < values.length - 1) { // there are values left
                s.append(", ");
            }
        }
        return s.toString();
    }

    public String getPositions() {
        return positions;
    }

    @Override
    public int compareTo(Violation<T> other) { // so I can use the built in Collections.sort()
        return Integer.compare(this.index, other.index);
    }
}
