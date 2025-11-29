package Checker;

import java.util.*;

public class RowChecker extends Checker {

    public RowChecker(int[] values, int num) {
        super(values, num);
    }

    @Override
    public void run() {
        findViolations(values);
    }

    @Override
    protected void findViolations(int[] numbers) {
        for (int j = 1; j <= 9; j++) {
            Set<Integer> positions = new HashSet<>();
            for (int k = 0; k < numbers.length; k++) {
                if (numbers[k] == j) {
                    positions.add(k + 1);
                }
            }
            if (positions.size() > 1) {
                Violation violation = new Violation('r', num, j, positions);
                rowViolations.add(violation);
            }
        }
    }
}