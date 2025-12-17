package Controller_Layer.Checker;

import Controller_Layer.Board.SodokuBoard;

public class IntegerVerifier {
    protected Integer[][] board;

    public IntegerVerifier(int[][] board) {
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

    public IntegerVerifier(Integer[][] board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null.");
        }
        this.board = board;
    }

    public Result<Integer> verify() {
        SodokuBoard<Integer> sodokuBoard = new SodokuBoard<>(9, board);
        SudokuIntegerChecker checker = new SudokuIntegerChecker(sodokuBoard);
        return checker.getViolations();
    }
}
