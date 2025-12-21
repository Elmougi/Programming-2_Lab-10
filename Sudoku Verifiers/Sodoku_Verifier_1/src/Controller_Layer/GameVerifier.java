package Controller_Layer;

import Controller_Layer.Board.SodokuBoard;
import Controller_Layer.Checker.BooleanMapAdapter;
import Controller_Layer.Checker.Result;
import Controller_Layer.Checker.SudokuIntVerifier;
import Controller_Layer.SolveIntegerGame.UnsolvedGameFlyweight;
import gameExceptions.InvalidGame;

/**
 * Responsible for game verification and solving.
 * Single Responsibility: Verification and solving operations.
 * REUSES: SudokuIntVerifier, BooleanMapAdapter, UnsolvedGameFlyweight
 */
public class GameVerifier {

    /**
     * Verify a game and return detailed result string.
     * REUSES: SudokuIntVerifier (existing class)
     */
    public String verifyGame(Game game) {
        // Use EXISTING SudokuIntVerifier
        SudokuIntVerifier verifier = new SudokuIntVerifier(game.board);
        Result<Integer> result = verifier.verify();

        return result.toString(); // Already formatted!
    }

    /**
     * Get boolean map showing which cells are valid/invalid.
     * REUSES: BooleanMapAdapter (existing class)
     */
    public boolean[][] getValidityMap(Game game) {
        // Use EXISTING BooleanMapAdapter
        SudokuIntVerifier verifier = new SudokuIntVerifier(game.board);
        Result<Integer> result = verifier.verify();

        // Convert to SodokuBoard
        Integer[][] integerBoard = convertToIntegerArray(game.board);
        SodokuBoard<Integer> board = new SodokuBoard<>(9, integerBoard);

        // Use EXISTING BooleanMapAdapter
        BooleanMapAdapter<Integer> adapter = new BooleanMapAdapter<>(board, result);
        return adapter.resultMap();
    }

    /**
     * Solve a game and return the solution.
     * REUSES: UnsolvedGameFlyweight (existing class)
     * Returns array encoding: [row, col, value, row, col, value, ...]
     */
    public int[] solveGame(Game game) throws InvalidGame {
        // Convert to Integer[][]
        Integer[][] integerBoard = convertToIntegerArray(game.board);

        // Use EXISTING UnsolvedGameFlyweight
        UnsolvedGameFlyweight solver = new UnsolvedGameFlyweight(9, integerBoard);
        int[][] solved = solver.solve();

        if (solved == null || !solver.isSolved()) {
            throw new InvalidGame("No solution exists for this game");
        }

        // Encode only the missing cells
        return encodeMissingCells(game.board, solved);
    }

    /**
     * Get the full solved board.
     * REUSES: UnsolvedGameFlyweight
     */
    public int[][] getSolvedBoard(Game game) throws InvalidGame {
        Integer[][] integerBoard = convertToIntegerArray(game.board);

        UnsolvedGameFlyweight solver = new UnsolvedGameFlyweight(9, integerBoard);
        int[][] solved = solver.solve();

        if (solved == null || !solver.isSolved()) {
            throw new InvalidGame("No solution exists for this game");
        }

        return solved;
    }

    // Helper methods
    private Integer[][] convertToIntegerArray(int[][] board) {
        Integer[][] result = new Integer[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                result[i][j] = board[i][j];
            }
        }
        return result;
    }

    /**
     * Encode only cells that were originally 0 (empty).
     * Format: [row, col, value, row, col, value, ...]
     */
    private int[] encodeMissingCells(int[][] original, int[][] solved) {
        // Count missing cells
        int missingCount = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (original[i][j] == 0) {
                    missingCount++;
                }
            }
        }

        // Encode: 3 ints per missing cell
        int[] encoded = new int[missingCount * 3];
        int index = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (original[i][j] == 0) {
                    encoded[index++] = i;
                    encoded[index++] = j;
                    encoded[index++] = solved[i][j];
                }
            }
        }

        return encoded;
    }
}