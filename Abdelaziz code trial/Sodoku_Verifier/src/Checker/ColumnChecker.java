package Checker;
import java.util.*;

public class ColumnChecker extends Checker {

    public ColumnChecker(int[] values, int num) {
        super(values, num);
    }

    @Override
    public void run() {
        
        findViolations(values);
    }

    public static int[] collectInts(int[][] board, int colIndex) {    
        int[] column = new int[9];
        for (int i = 0; i < 9; i++) {
            column[i] = board[i][colIndex];
        }
        return column;
    }

    @Override
    protected void findViolations(int[] numbers) {
        for(int j = 1; j <= 9; j++) {
            Set<Integer> positions = new HashSet<>();
            for(int k = 0; k < numbers.length; k++) {
                if(numbers[k] == j) {
                    positions.add(k + 1);
                }
            }
            if(positions.size() > 1) {
                Violation violation = new Violation('c', num, j, positions);
                addViolation(violation);
            }
        }
    }

    @Override
    protected synchronized void addViolation(Violation violation) {
        colViolations.add(violation);
    }
}