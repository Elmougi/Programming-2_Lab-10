package Controller_Layer.Checker;

import Controller_Layer.Board.SodokuBoard;
import gameExceptions.InvalidGame;

public class SudokuIntVerifier {
    protected Integer[][] board;

    public SudokuIntVerifier(int[][] board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null.");
        }

        this.board = new Integer[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = Integer.valueOf(board[i][j]);
            }
        }
    }

    public SudokuIntVerifier(Integer[][] board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null.");
        }
        this.board = board;
    }

    public Result<Integer> verify() {
        SodokuBoard<Integer> sodokuBoard = null;
        try {
            sodokuBoard = new SodokuBoard<>(9, board);
        } catch (InvalidGame e) {
            System.out.println("Provided board is not a valid Sudoku board:\n" + e.getMessage());
        }
        SudokuIntegerChecker checker = new SudokuIntegerChecker(sodokuBoard);
        return checker.getViolations();
    }
}
