package Controller_Layer.Board;

import gameExceptions.InvalidGame;

public class SodokuBoard<T> implements Board<T> {
    public final T[][] board;
    public final int SIZE;

    public SodokuBoard(int size, T[][] board) throws InvalidGame {
        this.SIZE = size;
        if (board.length != SIZE || board[0].length != SIZE) {
            throw new InvalidGame("Board must be " + SIZE + "x" + SIZE + ".");
        }
        this.board = board;
    }

    @Override
    public BoardIterator<T> rowIterator() {
        return new RowIterator<>(board);
    }

    @Override
    public BoardIterator<T> columnIterator() {
        return new ColumnIterator<>(board);
    }

    @Override
    public BoardIterator<T> boxIterator() {
        return new BoxIterator<>(board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(board[i][j]).append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int[][] getIntBoard() {
        if (!(board[0][0] instanceof Integer)) {
            throw new IllegalStateException("Board type is not Integer");
        }

        int[][] intBoard = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                intBoard[i][j] = (Integer) board[i][j];
            }
        }
        return intBoard;
    }
}
