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
    // Note: we know that the board is true except for the missing values
    // all lists are synced by index

    private boolean isSolved = false; // my observer variable

    public UnsolvedGameFlyweight(int size, Integer[][] board) throws InvalidGame {
        super(size, board);
        findMissingValuesData();
        // System.out.println("Found " + missingCount + " missing values");
        // //----------------------
    }

    private void findMissingValuesData() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    missingCount++;
                    if (missingCount > 5) {
                        throw new IllegalArgumentException("too many missing values. Max is 5");
                    }

                    rows.add(i);
                    cols.add(j);
                    int boxRow = i / (int) Math.sqrt(SIZE);
                    int boxCol = j / (int) Math.sqrt(SIZE);
                    int boxIndex = boxRow * (int) Math.sqrt(SIZE) + boxCol;
                    boxes.add(boxIndex);
                }
            }
        }
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void updateSolveStatus(int[] solution) { // part of the observer pattern -> using its spirit
        isSolved = (solution != null);
        if (isSolved) {
            // apply solution to board
            for (int i = 0; i < missingCount; i++) {
                board[rows.get(i)][cols.get(i)] = solution[i];
            }
        }
    }

    public int[][] solve() {
        if (rows.size() == 0) {
            isSolved = true;
            return super.getIntBoard();
        }

        PermutationIterator permIterator = new PermutationIterator(SIZE, missingCount);
        ArrayList<Thread> threads = new ArrayList<>();

        while (permIterator.hasNext() && !isSolved) {
            int[] valuesToTry = permIterator.next();
            SolutionFlyweightContext context = new SolutionFlyweightContext(this, valuesToTry);

            threads.add(new Thread(context));
            threads.get(threads.size() - 1).start();
        } // if solved no more threads will be created

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

        return null; // no solution found
    }

    // this method have been discarded
    private void OriginaltryAllPermutations(int[] valuesToTry, int position) {
        SolutionFlyweightContext context = new SolutionFlyweightContext(this, valuesToTry);
        if (context.solveBoard() != null) {
            isSolved = true;
        }

        // Base case: we've filled all positions or solved
        if (position == missingCount) {
            return;
        }
        if (isSolved) {
            return;
        }

        // trying all values for current position
        for (int value = 1; value <= SIZE; value++) {
            valuesToTry[position] = value;
            OriginaltryAllPermutations(valuesToTry, position + 1); // for each position's value try all values

            // if solved after the last call, stop
            if (isSolved) {
                return;
            }
        }
    }

    public static void main(String[] args) {
        // testCounter = 0;
        // TESTtryAllPermutations(new int[] { 1, 1, 1, 1, 1 }, 0);

        // System.out.println("Done testing permutations. Total tested: " + testCounter
        // + " out of max " + max);
    }

    // private static int testCounter = 0;
    // private static int max = (int) Math.pow(9, 5);

    // private static void TESTtryAllPermutations(int[] valuesToTry, int position) {
    // testCounter++;

    // // Base case: we've filled all positions or solved
    // if (position == 5 || testCounter >= max) {
    // return;
    // }

    // // trying all values for current position
    // for (int value = 1; value <= 9; value++) {
    // valuesToTry[position] = value;
    // TESTtryAllPermutations(valuesToTry, position + 1); // for each position's
    // value try all values

    // }
    // }
}
