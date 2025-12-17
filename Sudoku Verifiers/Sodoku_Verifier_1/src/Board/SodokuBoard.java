package Board;

public class SodokuBoard<T> implements Board<T> {
    public final T[][] board;
    public final int SIZE;

    public SodokuBoard(int size, T[][] board) {
        this.SIZE = size;
        if(board.length != SIZE || board[0].length != SIZE) {
            throw new IllegalArgumentException("Board must be " + SIZE + "x" + SIZE + ".");
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
}
