package Controller_Layer.SolveIntegerGame;

import java.util.ArrayList;
import java.util.List;
import Controller_Layer.Board.SodokuBoard;
import gameExceptions.InvalidGame;

public class UnsolvedGameFlyweight extends SodokuBoard<Integer> {
    // lists with missing values that needs validation
    protected List<Integer> cols = new ArrayList<>();
    protected List<Integer> rows = new ArrayList<>();
    protected List<Integer> boxes = new ArrayList<>();
    protected int missingCount = 0;

    // Volatile is generally preferred for visibility, but per constraints "no synchronized/volatile",
    // we rely on the memory model eventually propagating the change or the thread join visibility.
    private boolean isSolved = false;

    public UnsolvedGameFlyweight(int size, Integer[][] board) throws InvalidGame {
        super(size, board);
        findMissingValuesData();
    }

    private void findMissingValuesData() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    missingCount++;
                    rows.add(i);
                    cols.add(j);
                    int boxRow = i / (int) Math.sqrt(SIZE);
                    int boxCol = j / (int) Math.sqrt(SIZE);
                    int boxIndex = boxRow * (int) Math.sqrt(SIZE) + boxCol;
                    boxes.add(boxIndex);
                }
            }
        }
        // Constraint from PDF: Solver restricted to exactly 5 empty cells
        if (missingCount > 5) {
            throw new IllegalArgumentException("Solver is restricted to max 5 missing values. Found: " + missingCount);
        }
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void updateSolveStatus(int[] solution) {
        // Double check to prevent overwriting if multiple threads succeed simultaneously
        if (!isSolved && solution != null) {
            isSolved = true;
            // Apply solution to the internal board representation
            for (int i = 0; i < missingCount; i++) {
                board[rows.get(i)][cols.get(i)] = solution[i];
            }
        }
    }

    public int[][] solve() {
        if (missingCount == 0) {
            isSolved = true;
            return super.getIntBoard();
        }

        PermutationIterator permIterator = new PermutationIterator(SIZE, missingCount);
        ArrayList<Thread> threads = new ArrayList<>();

        // Create workers for permutations
        while (permIterator.hasNext()) {
            // Optimization: Stop creating threads if solved
            if (isSolved) break;

            int[] valuesToTry = permIterator.next();
            SolutionFlyweightContext context = new SolutionFlyweightContext(this, valuesToTry);

            Thread t = new Thread(context);
            threads.add(t);
            t.start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isSolved) {
            return super.getIntBoard();
        }

        return null; // No solution found
    }
}